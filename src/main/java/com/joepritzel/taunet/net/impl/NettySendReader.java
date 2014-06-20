package com.joepritzel.taunet.net.impl;

import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.internal.JSONWrapper;
import com.joepritzel.taunet.net.Send;

/**
 * Waits for Send objects to be given to the broker and then sends them over the
 * network.
 * 
 * @author Joe Pritzel
 *
 */
public class NettySendReader extends Subscriber<Send<?>> {

	/**
	 * Internal Gson object.
	 */
	private final Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
			.create();

	@Override
	public void receive(Send<?> message) {
		((NettyConnection) message.conn).channel.writeAndFlush(gson
				.toJson(new JSONWrapper(
						message.data.getClass().getSimpleName(), gson
								.toJson(message.data)))
				+ "\n");
	}
}