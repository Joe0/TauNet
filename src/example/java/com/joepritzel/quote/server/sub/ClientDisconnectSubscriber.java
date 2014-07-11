package com.joepritzel.quote.server.sub;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.quote.server.service.QuoteFinder;
import com.joepritzel.taunet.ConnectionDisconnected;

/**
 * Used to clean things up when a user disconnects.
 * 
 * @author Joe Pritzel
 *
 */
public class ClientDisconnectSubscriber extends
		Subscriber<ConnectionDisconnected> {

	/**
	 * The quote finder that we're using.
	 */
	private final QuoteFinder quoteFinder;

	/**
	 * Creates a new ClientDisconnectSubscriber with the given QuoteFinder.
	 * 
	 * @param quoteFinder
	 */
	public ClientDisconnectSubscriber(QuoteFinder quoteFinder) {
		this.quoteFinder = quoteFinder;
	}

	@Override
	public void receive(ConnectionDisconnected message) {
		// Remove the entry from the quote finder.
		quoteFinder.removeUser(message.getConnection().getID());
	}

}
