package com.joepritzel.quote.server.sub;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.quote.server.model.RequestQuote;
import com.joepritzel.quote.server.service.QuoteFinder;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.Send;

/**
 * Handles quote requests.
 * 
 * @author Joe Pritzel
 *
 */
public class RequestSubscriber extends Subscriber<RequestQuote> {

	/**
	 * The PSBroker we're using.
	 */
	private final PSBroker broker;

	/**
	 * The networking implementation we're using.
	 */
	private final NetworkingImplementation netImpl;

	/**
	 * The quote finder we're using.
	 */
	private final QuoteFinder quoteFinder;

	/**
	 * Creates a new RequestSubscriber with the given PSBroker,
	 * NetworkingImplementation, and QuoteFinder.
	 * 
	 * @param broker
	 *            - The PSBroker to use.
	 * @param netImpl
	 *            - The NetworkingImplementation to use.
	 * @param quoteFinder
	 *            - The QuoteFinder to use.
	 */
	public RequestSubscriber(PSBroker broker, NetworkingImplementation netImpl,
			QuoteFinder quoteFinder) {
		this.broker = broker;
		this.netImpl = netImpl;
		this.quoteFinder = quoteFinder;
	}

	@Override
	public void receive(RequestQuote message) {
		// Simply send the quote, from the QuoteFinder, to the connection with
		// the requested ID.
		broker.publish(new Send<String>(netImpl.getConnectionByID(message
				.getName()), quoteFinder.find(message.getName())));
	}

}
