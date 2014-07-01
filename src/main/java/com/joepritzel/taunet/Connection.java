package com.joepritzel.taunet;

/**
 * Represents a connection to a server or client.
 * 
 * @author Joe Pritzel
 *
 */
public interface Connection {

	/**
	 * The ID of the connection.
	 */
	public String getID();

	/**
	 * Changes the ID of the connection to the one provided.
	 * 
	 * @param id
	 *            - The new ID.
	 */
	public void setID(String id);

	/**
	 * Sends an encoded exception.
	 * 
	 * @param encodedException
	 *            - The exception.
	 */
	public void sendException(String encodedException);
}
