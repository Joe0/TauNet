package com.joepritzel.taunet.net.impl;

/**
 * Used by Netty to self-identify Connections.
 * 
 * @author Joe Pritzel
 *
 */
public class NettySelfID {

	/**
	 * The ID to use.
	 */
	public final String id;

	/**
	 * Creates a new NettySelfID object with the given ID.
	 * 
	 * @param id
	 *            - The ID to use.
	 */
	public NettySelfID(String id) {
		this.id = id;
	}

}
