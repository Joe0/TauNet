package com.joepritzel.taunet.net.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.taunet.Connection;
import com.joepritzel.taunet.ConnectionDisconnected;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.internal.JSONToObject;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.Send;

/**
 * A server-side NetworkingImplementation that uses Netty.
 * 
 * @author Joe Pritzel.
 *
 */
public class NettyTCPServerImplementation implements NetworkingImplementation {

	private String host;

	/**
	 * The port to be used.
	 */
	private int port;

	/**
	 * A group for the event group.
	 */
	private EventLoopGroup group;

	/**
	 * A collection of active clients connected to the server.
	 */
	private final ChannelGroup clients = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);

	/**
	 * The channel future that waits until closed.
	 */
	private ChannelFuture f;

	/**
	 * The PSBroker to be used.
	 */
	private PSBroker broker;

	/**
	 * The ID of this connection.
	 */
	private String id;

	/**
	 * The instance of NettySendReader being used.
	 */
	private NettySendReader<?> sendReader;

	public NettyTCPServerImplementation(String host, int port) {
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
			throw new IllegalStateException(
					"Can not call start more than once.");
		}
		this.id = id;
		sendReader = new NettySendReader<>();
		broker.subscribe(sendReader, Send.class);
		group = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(group).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				// .handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new StringDecoder(CharsetUtil.UTF_8),
								new StringEncoder(CharsetUtil.UTF_8),
								new JSONServerHandler());
					}
				});

		f = b.bind(host, port).sync();
	}

	/**
	 * The channel adapter to route the message to the JSONDecoder.
	 * 
	 * @author Joe Pritzel
	 *
	 */
	private class JSONServerHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			clients.add(ctx.channel());
			broker.publish(new Send<NettySelfID>(new NettyConnection(ctx
					.channel(), id), new NettySelfID(id)));
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			Connection conn = null;
			try {
				conn = new NettyConnection(ctx.channel(), ctx.channel().attr(
						NettyAttributes.idKey).get());
				broker.publish(new JSONToObject(conn, (String) msg));
			} catch (IDAlreadyConnectedException e) {
				try {
					if (conn == null) {
						conn = new NettyConnection(ctx.channel());
					}
					sendReader.exception(conn, e);
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) {
			ctx.flush();
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) {
			Connection conn = new NettyConnection(ctx.channel(), ctx.channel().attr(
					NettyAttributes.idKey).get());
			clients.remove(ctx.channel());
			broker.publish(new ConnectionDisconnected(conn));
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
		List<Connection> cl = new ArrayList<>();
		clients.forEach(c -> cl.add(new NettyConnection(c)));
		return cl;
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
			return true;
		}
		return false;
	}

	@Override
	public void addTypes(List<Class<?>> classTypeList) {
		classTypeList.add(NettySelfID.class);
	}

}
