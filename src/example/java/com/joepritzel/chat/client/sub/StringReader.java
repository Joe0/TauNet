package com.joepritzel.chat.client.sub;

import com.joepritzel.feather.Subscriber;

/**
 * Reads strings from the server and prints them to the console.
 * 
 * @author Joe Pritzel
 *
 */
public class StringReader extends Subscriber<String> {

	@Override
	public void receive(String message) {
		// Simply print the message to the console.
		System.out.println(message);
	}

}
