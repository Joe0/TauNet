package com.joepritzel.taunet;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.taunet.internal.JSONDecoder;
import com.joepritzel.taunet.internal.JSONToObject;
import com.joepritzel.taunet.net.NetworkingImplementation;

/**
 * @author Joe Pritzel
 *
 */
public class Server {
	/**
	 * The NetworkingImplementation that will be used.
	 */
	private final NetworkingImplementation netImpl;

	/**
	 * The PSBroker that will be used.
	 */
	private final PSBroker broker;

	/**
	 * The JSONDecoder to use.
	 */
	private final JSONDecoder decoder;

	/**
	 * Creates a Server object.
	 * 
	 * @param netImpl
	 *            - The NetworkingImplementation to use.
	 * @param broker
	 *            - The PSBroker to use.
	 */
	Server(NetworkingImplementation netImpl, PSBroker broker) {
		this.netImpl = netImpl;
		this.broker = broker;
		this.netImpl.setPSBroker(broker);
		this.decoder = new JSONDecoder(this.broker, this.netImpl);
		this.broker.subscribe(decoder, JSONToObject.class);
	}

	/**
	 * Returns the PSBroker that is used internally by this Server.
	 */
	public PSBroker getBroker() {
		return broker;
	}

	/**
	 * Starts the server with the given id. Must be called to start.
	 * 
	 * @param id
	 *            - The ID for the server.
	 * @throws Exception
	 */
	public void start(String id) throws Exception {
		netImpl.start(id);
	}

	/**
	 * Simply waits for the server to end. Doesn't need to be called.
	 * 
	 * @throws Exception
	 */
	public void waitForEnd() throws Exception {
		netImpl.waitUntilEnd();
	}

	/**
	 * Returns the NetworkingImplementation being used.
	 */
	public NetworkingImplementation getNetworkingImplementation() {
		return netImpl;
	}
}
