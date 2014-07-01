package com.joepritzel.taunet.net.impl;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.Connection;
import com.joepritzel.taunet.IDAlreadyConnectedException;
import com.joepritzel.taunet.internal.JSONEncoder;
import com.joepritzel.taunet.net.Send;

/**
 * Waits for Send objects to be given to the broker and then sends them over the
 * network.
 * 
 * @author Joe Pritzel
 *
 */
public class NettySendReader<T> extends Subscriber<Send<T>> {

	@Override
	public void receive(Send<T> message) {
		((NettyConnection) message.conn).channel.writeAndFlush(JSONEncoder
				.encode(message));
	}

	public void exception(Connection conn, IDAlreadyConnectedException e) {
		conn.sendException(JSONEncoder.encodeException(e));
	}
}