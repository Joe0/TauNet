package com.joepritzel.quote.client.sub;

import javax.swing.JOptionPane;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.quote.client.model.RequestQuote;
import com.joepritzel.taunet.net.Send;

/**
 * Listens for Strings and then prints them, and prompts the user to ask for a
 * new quote.
 * 
 * @author Joe pritzel
 *
 */
public class StringSubscriber extends Subscriber<String> {

	/**
	 * The PSBroker the application uses.
	 */
	private final PSBroker broker;

	/**
	 * The name of this client.
	 */
	private final String name;

	/**
	 * Creates a new StringSubscriber using the given broker and name.
	 * 
	 * @param broker
	 *            - The PSBroker this application uses.
	 * @param name
	 *            - The name of this client.
	 */
	public StringSubscriber(PSBroker broker, String name) {
		this.broker = broker;
		this.name = name;
	}

	@Override
	public void receive(String message) {
		// Simply prints the quote.
		System.out.println(message);

		// Prompts the user to get another quote. 
		int response = JOptionPane.showConfirmDialog(null,
				"Would you like to request another quote?", "Another Quote?",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			// Sends a new RequestQuote to the server.
			broker.publish(new Send<RequestQuote>(new RequestQuote(name)));
		} else {
			// Exits the application if the user doesn't want another quote.
			System.exit(0);
		}
	}

}
