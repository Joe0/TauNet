package com.joepritzel.quote.server.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Finds the next quote to send.
 * @author Joe Pritzel
 *
 */
public class QuoteFinder {

	/**
	 * A list of quotes to send. (Taken from the Netty quote of the moment example.)
	 */
	private final String[] quotes = {
			"Where there is love there is life.",
			"First they ignore you, then they laugh at you, then they fight you, then you win.",
			"Be the change you want to see in the world.",
			"The weak can never forgive. Forgiveness is the attribute of the strong." };
	
	/**
	 * A mapping of all connected users and the index of the next quote to send them.
	 */
	private final Map<String, Integer> lastQuote = new HashMap<>();

	/**
	 * Finds the next quote to send the user.
	 * @param name - The name of the client.
	 * @return A quote, unless there are none left, then it sends, "There are no more quotes left."
	 */
	public String find(String name) {
		// Get the index of the quote to send, will be null if not in the map.
		Integer last = lastQuote.get(name);
		
		if (last != null) { // If mapping was found.
			// Increment the index.
			lastQuote.put(name, last + 1);
			try {
				// Return the next quote.
				return quotes[last];
			} catch (ArrayIndexOutOfBoundsException e) {
				// When there are no more quotes, it returns the following
				return "There are no more quotes left.";
			}
		} else { // No mapping was found
			// Create a mapping
			lastQuote.put(name, 1);
			// Return the first quote.
			return quotes[0];
		}
	}
	
	/**
	 * Clears a users progress through the list. Usually called when the client disconnects.
	 * @param name - The name of the client to remove.
	 */
	public void removeUser(String name) {
		// Simply remove the mapping.
		lastQuote.remove(name);
	}

}
