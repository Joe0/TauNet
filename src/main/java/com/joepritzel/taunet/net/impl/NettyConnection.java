package com.joepritzel.taunet.net.impl;

import io.netty.channel.Channel;

import com.joepritzel.taunet.Connection;

/**
 * Represents a Connection through Netty.
 * 
 * @author Joe Pritzel
 *
 */
public class NettyConnection implements Connection {

	/**
	 * The channel to use.
	 */
	public final Channel channel;

	/**
	 * Tje ID to use.
	 */
	private String id;

	/**
	 * Creates a new NettyConnection, that uses the already set ID.
	 * 
	 * @param channel
	 *            - The channel to use.
	 */
	public NettyConnection(Channel channel) {
		this(channel, getIDCons(channel));
	}

	/**
	 * Used in the constructor to get the default ID.
	 */
	private static String getIDCons(Channel channel) {
		String id =  channel.attr(NettyAttributes.idKey).get();
		if(id == null) {
			return "";
		}
		return id;
	}

	/**
	 * Creates a new NettyConnection, that uses the provided ID.
	 * 
	 * @param channel
	 * @param id
	 */
	public NettyConnection(Channel channel, String id) {
		this.channel = channel;
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public void setID(String id) {
		this.id = id;
		this.channel.attr(NettyAttributes.idKey).set(id);
	}

}
