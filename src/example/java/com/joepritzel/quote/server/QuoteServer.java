package com.joepritzel.quote.server;

import com.joepritzel.quote.server.model.RequestQuote;
import com.joepritzel.quote.server.service.QuoteFinder;
import com.joepritzel.quote.server.sub.ClientDisconnectSubscriber;
import com.joepritzel.quote.server.sub.RequestSubscriber;
import com.joepritzel.taunet.ConnectionDisconnected;
import com.joepritzel.taunet.Server;
import com.joepritzel.taunet.ServerBuilder;

public class QuoteServer {
	public static void main(String[] args) throws Exception {
		// Creates a new server.
		Server server = new ServerBuilder().
				setPort(80). // Change me if needed, if not you can remove
				build();

		// Creates a new quotefinder instance.
		QuoteFinder finder = new QuoteFinder();

		// Registers our subscribers.
		server.getBroker().subscribe(
				new RequestSubscriber(server.getBroker(),
						server.getNetworkingImplementation(), finder),
				RequestQuote.class);
		server.getBroker().subscribe(new ClientDisconnectSubscriber(finder),
				ConnectionDisconnected.class);

		// Start the server.
		server.start("quote server");
		
		// Simply wait for it to end.
		server.waitForEnd();
	}
}
