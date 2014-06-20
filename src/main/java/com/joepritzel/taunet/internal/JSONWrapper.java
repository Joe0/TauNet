package com.joepritzel.taunet.internal;

/**
 * Wraps a simple class name and JSON data for it together.
 * 
 * @author Joe Pritzel
 *
 */
public class JSONWrapper {

	/**
	 * The simple class name.
	 */
	public final String className;

	/**
	 * The JSON data for the given class.
	 */
	public final String json;

	/**
	 * Creates a new JSON wrapper for data.
	 * 
	 * @param className
	 *            - The simple class name of the data.
	 * @param json
	 *            - The JSON data representing the class.
	 */
	public JSONWrapper(String className, String json) {
		this.className = className;
		this.json = json;
	}

}
