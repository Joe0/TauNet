package com.joepritzel.taunet.net.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.taunet.Connection;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.internal.JSONToObject;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.Send;

/**
 * A client-side NetworkingImplementation that uses Netty.
 * 
 * @author Joe Pritzel.
 *
 */
public class NettyTCPClientImplementation implements NetworkingImplementation {

	/**
	 * The host to be used.
	 */
	private String host;

	/**
	 * The port to be used.
	 */
	private int port;

	/**
	 * The channel future that waits until closed.
	 */
	private ChannelFuture f;

	/**
	 * A group for the event group.
	 */
	private EventLoopGroup group;

	/**
	 * The channel in which the Client is connected to the Server.
	 */
	private Channel serverChannel;

	/**
	 * The ID of this connection.
	 */
	private String id;

	/**
	 * The PSBroker to be used.
	 */
	private PSBroker broker;

	/**
	 * Used to block until ready.
	 */
	private boolean sendSet = false;

	public NettyTCPClientImplementation(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void setPSBroker(PSBroker broker) {
		if (f != null) {
			throw new IllegalStateException(
					"Can not change PSBroker once running.");
		}
		this.broker = broker;
	}

	@Override
	public void start(String id) throws Exception {
		if (f != null) {
			throw new IllegalStateException("Can only call start once.");
		}
		this.id = id;
		broker.subscribe(new NettySendReader<>(), Send.class);
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new StringDecoder(CharsetUtil.UTF_8),
								new StringEncoder(CharsetUtil.UTF_8),
								new JSONClientHandler());
					}
				});

		f = b.connect(host, port).sync();
		while (!sendSet) {
			Thread.sleep(100);
		}
	}

	/**
	 * The channel adapter to route the message to the JSONDecoder.
	 * 
	 * @author Joe Pritzel
	 *
	 */
	private class JSONClientHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			Send.setDefaultConnection(new NettyConnection(ctx.channel()));
			serverChannel = ctx.channel();
			broker.publish(new Send<NettySelfID>(new NettyConnection(ctx
					.channel()), new NettySelfID(id)));
			sendSet = true;
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			Connection conn = new NettyConnection(ctx.channel());
			try {
				conn = new NettyConnection(ctx.channel(), ctx.attr(
						NettyAttributes.idKey).get());
			} catch (Exception e) {
				// Don't care, already set.
			}
			broker.publish(new JSONToObject(conn, (String) msg));
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
			ctx.flush();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			broker.publish(cause);
		}
	}

	@Override
	public void waitUntilEnd() throws Exception {
		try {
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	@Override
	public List<Connection> getConnections() {
		List<Connection> clients = new ArrayList<>();
		clients.add(new NettyConnection(serverChannel));
		return clients;
	}

	@Override
	public Connection getConnectionByID(String id) {
		try {
			return getConnections().stream().filter(c -> c.getID().equals(id))
					.findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public void setIP(String ip) {
		if (f != null) {
			throw new IllegalStateException("Can not change IP once running.");
		}
		this.host = ip;
	}

	@Override
	public void setPort(int port) {
		if (f != null) {
			throw new IllegalStateException("Can not change IP once running.");
		}
		this.port = port;
	}

	@Override
	public boolean isInternalMessage(Connection conn, Object o) {
		if (o instanceof NettySelfID) {
			NettySelfID id = (NettySelfID) o;
			if (getConnectionByID(id.id) == null) {
				((NettyConnection) conn).channel.attr(NettyAttributes.idKey)
						.set(id.id);
			} else {
				throw new IDAlreadyConnectedException(id.id);
			}
		}
		return false;
	}

	@Override
	public void addTypes(List<Class<?>> classTypeList) {
		classTypeList.add(NettySelfID.class);
	}
}
