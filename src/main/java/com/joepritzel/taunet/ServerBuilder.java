package com.joepritzel.taunet;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.PSBrokerBuilder;
import com.joepritzel.feather.strategy.subscribe.FastSubscribeUnsubscribe;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;
import com.joepritzel.taunet.internal.SubscriberHook;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.impl.NettyTCPServerImplementation;

/**
 * A builder for the Server class.<br>
 * <br>
 * By default, it will use port 80 and TCP with Netty.
 * 
 * @author Joe Pritzel
 *
 */
public class ServerBuilder {

	/**
	 * The NetworkingImplementation that will be used by the Server.
	 */
	private NetworkingImplementation netImpl;

	/**
	 * The PSBroker that will be used by the Server.
	 */
	private PSBroker broker;

	/**
	 * The host that will be used.
	 */
	private String host;
	
	/**
	 * The port that will be used.
	 */
	private int port;
	
	/**
	 * An instance to the SubscriberHook.
	 */
	private SubscriberHook ss;


	/**
	 * Creates a new ServerBuilder, used for creating instances of Server.
	 */
	public ServerBuilder() {
		host = "127.0.0.1";
		port = 80;
		ss = new SubscriberHook(new FastSubscribeUnsubscribe());
		this.broker = new PSBrokerBuilder().subscribeStrategy(ss).build();
		ss.setBroker(this.broker);
		this.netImpl = new NettyTCPServerImplementation("127.0.0.1", 80);
	}

	/**
	 * Must provide the PSBrokerBuilder just before you call the build method,
	 * and the SubscribeStrategy that it is going to use.
	 * 
	 * @param brokerBuilder - The PSBrokerBuilder that will be used.
	 * @param ss - The SubscriberStrategy that will be used.
	 */
	public ServerBuilder setPSBroker(PSBrokerBuilder brokerBuilder,
			SubscribeStrategy ss) {
		SubscriberHook sh = new SubscriberHook(ss);
		this.broker = brokerBuilder.subscribeStrategy(sh).build();
		sh.setBroker(this.broker);
		return this;
	}

	/**
	 * Sets the NetworkingImplementation the Server should use.
	 * 
	 * @param netImpl
	 *            - The NetworkingImplementation to use.
	 */
	public ServerBuilder setNetworkingImplementation(
			NetworkingImplementation netImpl) {
		this.netImpl = netImpl;
		return this;
	}

	/**
	 * Sets the host that the Server will bind to.
	 * 
	 * @param host
	 *            - The host to bind to.
	 */
	public ServerBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Sets the port that the Server will bind to.
	 * 
	 * @param port
	 *            - The port to bind to.
	 */
	public ServerBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Creates the Server with the current settings used by this ServerBuilder.
	 */
	public Server build() {
		netImpl.setIP(host);
		netImpl.setPort(port);
		return new Server(netImpl, broker);
	}
}
