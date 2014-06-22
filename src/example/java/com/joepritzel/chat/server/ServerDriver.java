package com.joepritzel.chat.server;

import com.joepritzel.chat.server.sub.StringReader;
import com.joepritzel.taunet.Server;
import com.joepritzel.taunet.ServerBuilder;

/**
 * The driver for the server.
 * 
 * @author Joe Pritzel
 *
 */
public class ServerDriver {
	public static void main(String[] args) throws Exception {
		// Create a server.
		Server server = new ServerBuilder().build();

		// Register our subscribers.
		server.getBroker().subscribe(
				new StringReader(server.getBroker(),
						server.getNetworkingImplementation()), String.class);

		// Start the server and wait to end.
		try {
			server.start("chat server");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.waitForEnd();
		}
	}
}
