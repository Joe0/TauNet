package com.joepritzel.quote.client.sub;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.quote.client.QuoteClient;
import com.joepritzel.taunet.IDAlreadyConnectedException;

/**
 * A class to catch the exception for someone with the same id being connected.
 * 
 * @author Joe Pritzel
 *
 */
public class AlreadyConnected extends Subscriber<IDAlreadyConnectedException> {

	@Override
	public void receive(IDAlreadyConnectedException message) {
		// Start a new one.
		new QuoteClient().run();
	}

}