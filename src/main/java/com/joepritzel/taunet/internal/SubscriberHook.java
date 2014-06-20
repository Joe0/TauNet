package com.joepritzel.taunet.internal;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.internal.SubscriberParent;
import com.joepritzel.feather.strategy.subscribe.SubscribeStrategy;
import com.joepritzel.taunet.internal.JSONDecoder.JSONDecoderRegister;

/**
 * Hooks into a SubscribeStrategy and informs the JSONDecoder of new types
 * registered.
 * 
 * @author Joe Pritzel
 *
 */
public class SubscriberHook implements SubscribeStrategy {

	/**
	 * The SubscribeStrategy to hook.
	 */
	private final SubscribeStrategy strat;

	/**
	 * The PSBroker to send the JSONDecoderRegister to.
	 */
	private PSBroker broker;

	/**
	 * Creates a new SubscriberHook.
	 * 
	 * @param strat
	 *            - The SubscribeStrategy to hook.
	 */
	public SubscriberHook(SubscribeStrategy strat) {
		this.strat = strat;
	}

	@Override
	public <T> void subscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<?> subscriber, Class<T> messageType) {
		if (!messageType.isAssignableFrom(JSONDecoderRegister.class))
			broker.publish(new JSONDecoderRegister(messageType));
		strat.subscribe(mapping, subscriber, messageType);
	}

	@Override
	public <T> List<Subscriber<T>> unsubscribeAll(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Class<T> messageType) {
		// TODO: Unregister from JSONDecoder.
		return strat.unsubscribeAll(mapping, messageType);
	}

	@Override
	public <S extends Subscriber<T>, T> boolean unsubscribeByTypes(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			S subscriberType, Class<T> messageType) {
		// TODO: Unregister from JSONDecoder.
		return strat.unsubscribeByTypes(mapping, subscriberType, messageType);
	}

	@Override
	public <T> boolean unsubscribe(
			ConcurrentMap<Class<?>, List<SubscriberParent>> mapping,
			Subscriber<T> s, Class<T> messageType) {
		return strat.unsubscribe(mapping, s, messageType);
	}

	/**
	 * Sets the PSBroker to use.
	 * 
	 * @param broker
	 *            - The PSBroker to use.
	 */
	public void setBroker(PSBroker broker) {
		this.broker = broker;
	}

}
