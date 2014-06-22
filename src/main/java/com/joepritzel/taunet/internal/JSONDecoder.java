package com.joepritzel.taunet.internal;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.net.NetworkingImplementation;
import com.joepritzel.taunet.net.impl.NettyAttributes;
import com.joepritzel.taunet.net.impl.NettySelfID;

/**
 * Used to decode JSON objects to the original object sent across the network.
 * 
 * @author Joe Pritzel
 *
 */
public class JSONDecoder extends Subscriber<JSONToObject> {

	/**
	 * The instance of PSBroker to use.
	 */
	private final PSBroker broker;

	/**
	 * The instance of NetworkingImplementation to use.
	 */
	private final NetworkingImplementation netImpl;

	/**
	 * A collection of possible class types to check.
	 */
	private final List<Class<?>> classTypeList = new ArrayList<>();

	/**
	 * Creates a new JSONDecoder with the given PSBroker.
	 * 
	 * @param broker
	 *            - The PSBroker to use.
	 */
	public JSONDecoder(PSBroker broker, NetworkingImplementation netImpl) {
		this.broker = broker;
		this.netImpl = netImpl;
		broker.subscribe(new JSONDecoderRegisterReader(),
				JSONDecoderRegister.class);
		classTypeList.add(NettySelfID.class);
		classTypeList.add(IDAlreadyConnectedException.class);
	}

	/**
	 * Internal instance of Gson to use.
	 */
	private final Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
			.create();

	@Override
	public void receive(JSONToObject message) {
		final JSONWrapper w = gson.fromJson(message.json, JSONWrapper.class);
		classTypeList
				.stream()
				.filter(c -> c.getSimpleName().equals(w.className))
				.forEach(
						e -> {
							try {
								Object o = gson.fromJson(w.json, e);
								if (o instanceof NettySelfID) {
									NettySelfID id = (NettySelfID) o;
									if (netImpl.getConnectionByID(id.id) == null) {
										message.channel.attr(
												NettyAttributes.idKey).set(
												id.id);
									} else {
										try {
											message.channel.writeAndFlush(
													new IDAlreadyConnectedException(id.id))
													.sync();
										} catch (Exception e1) {
											// Don't care, closing anyway.
										} finally {
											message.channel.close();
										}
									}
								} else {
									broker.publish(o);
								}
							} catch (JsonSyntaxException e1) {
								// Don't care, we'll find one.
							}
						});
	}

	/**
	 * A value object used to register class types with the JSONDecoder.
	 * 
	 * @author Joe Pritzel
	 *
	 */
	static class JSONDecoderRegister {
		public final Class<?> clazz;

		/**
		 * Creates a new value object to hold a class type.
		 * 
		 * @param clazz
		 *            - The class type to hold.
		 */
		public JSONDecoderRegister(Class<?> clazz) {
			this.clazz = clazz;
		}
	}

	/**
	 * Waits for JSONDeoderRegister objects to be submitted to the PSBroker,
	 * then registers the held class to the JSONDecoder.
	 * 
	 * @author Joe Pritzel
	 *
	 */
	class JSONDecoderRegisterReader extends Subscriber<JSONDecoderRegister> {

		@Override
		public void receive(JSONDecoderRegister message) {
			if (!classTypeList.contains(message.clazz))
				classTypeList.add(message.clazz);
		}

	}

}
