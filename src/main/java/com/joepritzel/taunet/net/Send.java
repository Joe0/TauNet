package com.joepritzel.taunet.net;

import com.joepritzel.taunet.Connection;

/**
 * Used to send data across the network, and to the given Connection.
 * 
 * @author Joe Pritzel
 *
 * @param <T>
 *            - The type of data that will be sent.
 */
public class Send<T> {

	/**
	 * A default connection to send to. Should only be used by clients or
	 * servers that only accept 1 connection.
	 */
	private static Connection defaultConn;

	/**
	 * Sets the default connections. Should only be used by clients or servers
	 * that only accept 1 connection.
	 * 
	 * @param conn
	 *            - The connection that should be defaulted to.
	 */
	public static void setDefaultConnection(Connection conn) {
		defaultConn = conn;
	}

	/**
	 * The connection to send data to.
	 */
	public final Connection conn;

	/**
	 * The data to be sent.
	 */
	public final T data;

	/**
	 * Creates a new Send object with the given data, which will be sent to the
	 * default connection.
	 * 
	 * @param data
	 *            - The data to send.
	 */
	public Send(T data) {
		this.data = data;
		this.conn = defaultConn;
	}

	/**
	 * Creates a new Send object with the given data, which will be sent to the
	 * given connection.
	 * 
	 * @param conn
	 *            - The connection to send the data to.
	 * @param data
	 *            - The data to send.
	 */
	public Send(Connection conn, T data) {
		this.conn = conn;
		this.data = data;
	}
}
