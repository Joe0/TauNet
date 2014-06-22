package com.joepritzel.chat.client;

import javax.swing.JOptionPane;

import com.joepritzel.chat.client.sub.AlreadyConnected;
import com.joepritzel.chat.client.sub.StringReader;
import com.joepritzel.taunet.Client;
import com.joepritzel.taunet.ClientBuilder;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.net.Send;

/**
 * The driver for the client.
 * 
 * @author Joe Pritzel
 *
 */
public class ClientDriver {

	/**
	 * Used to break out of the loop if the name was bad and we need a new one.
	 */
	public boolean badName = false;

	public static void main(String[] args) {
		new ClientDriver().run();
	}

	/**
	 * Contains all the contents of the application, call if we should restart.
	 */
	public void run() {
		Client client = new ClientBuilder().build(); // Creates the client
														// connection.

		// Register our subscribers.
		client.getBroker().subscribe(new StringReader(), String.class);
		client.getBroker().subscribe(new AlreadyConnected(this),
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
		
		// Keep prompting user for new messages.
		while (!badName) {
			String msg = JOptionPane.showInputDialog(null,
					"Enter a message to send", "");
			if (msg == null || msg.length() < 1) {
				break;
			}
			client.getBroker().publish(new Send<String>(username + ": " + msg));
		}
	}
}
