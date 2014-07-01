package com.joepritzel.taunet.internal;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.net.Send;

/**
 * A class used to encode data.
 * 
 * @author Joe Pritzel
 *
 */
public class JSONEncoder {

	/**
	 * Internal Gson object.
	 */
	private static final Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
			.create();

	/**
	 * Encodes IDAlreadyConnectedException.
	 * 
	 * @param e
	 *            - The exception to encode.
	 */
	public static String encodeException(IDAlreadyConnectedException e) {
		return gson.toJson(new JSONWrapper(e.getClass().getSimpleName(), gson
				.toJson(e)));
	}

	/**
	 * Encodes data to be sent across the network.
	 * 
	 * @param data
	 *            - The data to be encoded.
	 */
	public static <T> String encode(Send<T> data) {
		return gson.toJson(new JSONWrapper(
				data.data.getClass().getSimpleName(), gson.toJson(data.data)));
	}
}
