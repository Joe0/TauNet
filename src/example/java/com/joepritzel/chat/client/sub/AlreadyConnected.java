package com.joepritzel.chat.client.sub;

import com.joepritzel.chat.client.ClientDriver;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.IDAlreadyConnectedException;

/**
 * A class to catch the exception for someone with the same id being connected.
 * 
 * @author Joe Pritzel
 *
 */
public class AlreadyConnected extends Subscriber<IDAlreadyConnectedException> {

	/**
	 * The currently running ClientDriver.
	 */
	private final ClientDriver clientDriver;

	/**
	 * Creates a new AlreadyConnected subscriber.
	 * 
	 * @param clientDriver
	 *            - The currently executing ClientDriver.
	 */
	public AlreadyConnected(ClientDriver clientDriver) {
		this.clientDriver = clientDriver;
	}

	@Override
	public void receive(IDAlreadyConnectedException message) {
		// Terminate the old client driver.
		clientDriver.badName = true;
		
		// Start a new one.
		new ClientDriver().run();
	}

}
