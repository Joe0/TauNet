package com.joepritzel.taunet.internal;

import io.netty.channel.Channel;

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
	public final Channel channel;

	/**
	 * The data to hold.
	 */
	public final String json;

	/**
	 * Creates a new value object to hold the data and channel.
	 * 
	 * @param channel
	 *            - The channel that is being used.
	 * @param json
	 *            - The data that is being sent.
	 */
	public JSONToObject(Channel channel, String json) {
		this.json = json;
		this.channel = channel;
	}
}