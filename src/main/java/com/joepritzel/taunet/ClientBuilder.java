package com.joepritzel.taunet;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.PSBrokerBuilder;
import com.joepritzel.feather.strategy.subscribe.FastSubscribeUnsubscribe;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;
import com.joepritzel.taunet.internal.SubscriberHook;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.impl.NettyTCPClientImplementation;

/**
 * A builder for the Client class.<br>
 * <br>
 * By default, it will use port 80 and TCP with Netty.
 * 
 * @author Joe Pritzel
 *
 */
public class ClientBuilder {

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
	 * Creates a new ClientBuilder, used for creating instances of Client.
	 */
	public ClientBuilder() {
		host = "127.0.0.1";
		port = 80;
		ss = new SubscriberHook(new FastSubscribeUnsubscribe());
		this.broker = new PSBrokerBuilder().subscribeStrategy(ss).build();
		ss.setBroker(this.broker);
		this.netImpl = new NettyTCPClientImplementation("127.0.0.1", 80);
	}

	/**
	 * Must provide the PSBrokerBuilder just before you call the build method,
	 * and the SubscribeStrategy that it is going to use.
	 * 
	 * @param brokerBuilder
	 *            - The PSBrokerBuilder that will be used.
	 * @param ss
	 *            - The SubscriberStrategy that will be used.
	 */
	public ClientBuilder setPSBroker(PSBrokerBuilder brokerBuilder,
			SubscribeStrategy ss) {
		SubscriberHook sh = new SubscriberHook(ss);
		this.broker = brokerBuilder.subscribeStrategy(sh).build();
		sh.setBroker(this.broker);
		return this;
	}

	/**
	 * Sets the NetworkingImplementation the Client should use.
	 * 
	 * @param netImpl
	 *            - The NetworkingImplementation to use.
	 */
	public ClientBuilder setNetworkingImplementation(
			NetworkingImplementation netImpl) {
		this.netImpl = netImpl;
		return this;
	}

	/**
	 * Sets the host that the Client will connect to.
	 * 
	 * @param host
	 *            - The host to connect to.
	 */
	public ClientBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Sets the port that the client will connect to.
	 * 
	 * @param port
	 *            - The port to connect to.
	 */
	public ClientBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Creates the Client with the current settings used by this ClientBuilder.
	 */
	public Client build() {
		netImpl.setIP(host);
		netImpl.setPort(port);
		return new Client(netImpl, broker);
	}
}
