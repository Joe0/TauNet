package com.joepritzel.taunet.net;

import java.util.List;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.taunet.Connection;

/**
 * An interface that all NetworkingImplementations must use. Used to abstract
 * away the internal details of a networking layer.<br>
 * <br>
 * When a connection disconnects from a server, it must publish a
 * ConnectionDisconnected object to the PSBroker.
 * 
 * @author Joe Pritzel
 *
 */
public interface NetworkingImplementation {

	/**
	 * Starts the NetworkingImplementation.
	 * 
	 * @param id
	 *            - The ID of this Connection.
	 * @throws Exception
	 *             - Can throw any type of Exception. Represents catastrophic
	 *             failure.
	 */
	public void start(String id) throws Exception;

	/**
	 * Sets the PSBroker to use.
	 * 
	 * @param broker
	 *            - The PSBroker to use.
	 */
	public void setPSBroker(PSBroker broker);

	/**
	 * Simply blocks the thread until the NetworkingImplementation ends.
	 * 
	 * @throws Exception
	 *             - Can throw any type of Exception. Represents catastrophuc
	 *             failure.
	 */
	public void waitUntilEnd() throws Exception;

	/**
	 * Returns a List of all active Connections.
	 */
	public List<Connection> getConnections();

	/**
	 * Returns an active Connection given the ID of a valid one. Null if one is
	 * not found by that ID.
	 * 
	 * @param id
	 *            - The ID of the active connection to look for.
	 */
	public Connection getConnectionByID(String id);

	/**
	 * Sets the IP to connect or bind to.
	 * 
	 * @param ip
	 *            - The IP to connect or bind to.
	 */
	public void setIP(String ip);

	/**
	 * Sets the port to connect or bind to.
	 * 
	 * @param port
	 *            - The port to connect or bind to.
	 */
	public void setPort(int port);

	/**
	 * Returns true if it is an internal message. Examples of these are messages
	 * to set connection IDs.
	 * 
	 * @param conn
	 *            - The connection.
	 * @param message
	 *            - The message.
	 */
	public boolean isInternalMessage(Connection conn, Object message);

	/**
	 * Adds internal types that need to be decoded, but may not yet or ever have
	 * a subscriber listening for them.
	 * 
	 * @param classTypeList
	 *            - The list to add types to.
	 */
	public void addTypes(List<Class<?>> classTypeList);
}
