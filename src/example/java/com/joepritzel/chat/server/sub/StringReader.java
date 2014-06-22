package com.joepritzel.chat.server.sub;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.Send;

/**
 * Reads strings from clients and sends them to every client.
 * 
 * @author Joe Pritzel
 *
 */
public class StringReader extends Subscriber<String> {

	/**
	 * The PSBroker that is being used.
	 */
	private PSBroker broker;

	/**
	 * The NetworkingImplementation that is being used.
	 */
	private final NetworkingImplementation netImpl;

	/**
	 * Creates a new StringReader subscriber.
	 * 
	 * @param broker
	 *            - The broker that is being used.
	 * @param netImpl
	 *            - The NetworkingImplementation that is being used.
	 */
	public StringReader(PSBroker broker, NetworkingImplementation netImpl) {
		this.broker = broker;
		this.netImpl = netImpl;
	}

	@Override
	public void receive(String message) {
		// Iterate all connections and send the message we received.
		netImpl.getConnections().forEach(
				c -> this.broker.publish(new Send<String>(c, message)));
	}

}
