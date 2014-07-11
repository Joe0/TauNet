package com.joepritzel.quote.client;

import javax.swing.JOptionPane;

import com.joepritzel.quote.client.model.RequestQuote;
import com.joepritzel.quote.client.sub.AlreadyConnected;
import com.joepritzel.quote.client.sub.StringSubscriber;
import com.joepritzel.taunet.Client;
import com.joepritzel.taunet.ClientBuilder;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.net.Send;

/**
 * A basic quote client. It will connect to the server and print out quotes.
 * 
 * @author Joe Pritzel
 *
 */
public class QuoteClient {

	public static void main(String[] args) {
		new QuoteClient().run();
	}

	public void run() {
		// Create a new client.
		Client client = new ClientBuilder().
				setHost("127.0.0.1"). // Change me if needed, if not you can remove
				setPort(80).		  // Change me if needed, if not you can remove
				build();
		client.getBroker().subscribe(new AlreadyConnected(),
				IDAlreadyConnectedException.class);
		
		// Set username.
		String username = "";
		while (username == null || username.equals("")) {
			username = JOptionPane.showInputDialog(null,
					"What is your username?", "");
		}
		
		// Connect to server.
		try {
			client.start(username);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Register the subscriber
		client.getBroker().subscribe(
				new StringSubscriber(client.getBroker(), username), String.class);
		
		// Send the initial request
		client.getBroker().publish(
				new Send<RequestQuote>(new RequestQuote(username)));
		
		// Simply wait for the connection to end
		try {
			client.waitForEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
