package com.joepritzel.taunet;

/**
 * This is published to the broker when a Connection disconnects from the
 * server.
 * 
 * @author Joe Pritzel
 *
 */
public class ConnectionDisconnected {

	/**
	 * The connection that disconnected.
	 */
	private final Connection conn;

	/**
	 * Creates a new ConnectionDisconnected instance with the given Connection.
	 * 
	 * @param conn
	 *            - The connection that disconnected.
	 */
	public ConnectionDisconnected(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @return The connection that disconnected.
	 */
	public Connection getConnection() {
		return this.conn;
	}

}
