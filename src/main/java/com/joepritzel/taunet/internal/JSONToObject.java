package com.joepritzel.taunet.internal;

import com.joepritzel.taunet.Connection;

/**
 * A value object to hold the JSON data sent across the network and channel in
 * which this was sent across.
 * 
 * @author Joe Pritzel
 *
 */
public class JSONToObject {

	/**
	 * The channel being sent over.
	 */
	public final Connection channel;

	/**
	 * The data to hold.
	 */
	public final String json;

	/**
	 * Creates a new value object to hold the data and channel.
	 * 
	 * @param conn
	 *            - The channel that is being used.
	 * @param json
	 *            - The data that is being sent.
	 */
	public JSONToObject(Connection conn, String json) {
		this.json = json;
		this.channel = conn;
	}
}