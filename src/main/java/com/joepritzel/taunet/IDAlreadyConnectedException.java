package com.joepritzel.taunet;

/**
 * Represents an exception where the ID was already bound. All IDs must be
 * unique.
 * 
 * @author Joe Pritzel
 *
 */
public class IDAlreadyConnectedException extends RuntimeException {

	/**
	 * Creates an IDAlreadyConnectedException with the given ID.
	 * 
	 * @param id
	 *            - The ID to specify in the message.
	 */
	public IDAlreadyConnectedException(String id) {
		super(id + " was already connected, all IDs must be unique.", null);
	}

	/**
	 * Randomly generated UID.
	 */
	private static final long serialVersionUID = 6272876823080508050L;


}
