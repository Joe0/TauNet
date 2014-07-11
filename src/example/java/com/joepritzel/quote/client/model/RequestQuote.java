package com.joepritzel.quote.client.model;

/**
 * Represents a request to the server for a quote.
 * 
 * @author Joe Pritzel
 *
 */
public class RequestQuote {

	
	/**
	 * The name of this client.
	 */
	@SuppressWarnings("unused")	// Never used by the client.
	private final String name;

	/**
	 * Creates a new RequestQuote to send to the server.
	 * 
	 * @param name
	 *            - The name of this client.
	 */
	public RequestQuote(String name) {
		this.name = name;
	}
}
