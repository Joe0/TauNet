TauNet
======

A super easy to use networking library.

Table of contents:
+ [Features](#features)
+ [Usage](#usage)
+ [License](#license)

<h2 name="features">Features</h2>

+ <strong>Ease of Use</strong> - TauNet is made to be easy to use, even for beginners, as it helps enforce good programming practices by design. A lot of things are done 'magically' behind the scenes, so beginners don't need to worry about it.
+ <strong>No Explicit Bindings</strong> - TauNet uses information you're already giving it to figure out what types are bound to messages sent across the network. This gets rid of the awkward bindings that most other libraries need, as well as the shared classes. The only thing that needs to be done, is create a Subscriber that listens for the specified message, and make sure the simple name and fields match.  
+ <strong>Event Driven</strong> - TauNet is event driven at the core, and requires the use of a publish-subscribe pattern.
+ <strong>Synchronous Feel</strong> - Even though TauNet is asynchronous at heart, it seems as if you're doing everything in synchronous.
+ <strong>Fast</strong> - At the core of TauNet is [Netty](http://netty.io/), [Feather](https://github.com/Joe0/Feather), and [Gson](https://code.google.com/p/google-gson/); all of which have been proven to be fast and reliable.

<h2 name="usage">Usage</h2>
How to create a basic chat server:
```Java
import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.Server;
import com.joepritzel.taunet.ServerBuilder;
import com.joepritzel.taunet.net.Send;

public class ChatServer {

	public static void main(String[] args) throws Exception {
		Server  server = new ServerBuilder().build(); // Create a server.
		server.start("server"); // Start the server and give it a name.
		PSBroker broker = server.getBroker(); // Get the PSBroker for easy use.
		Subscriber<String> stringSub = new Subscriber<String>() { // Create a new subscriber, usually defined in its own class.
			@Override
			public void receive(String msg) {
				// Send the message to all active connections.
				server.getNetworkingImplementation().getConnections()
						.forEach(c -> 
							broker.publish(new Send<String>(c, msg)) // Send a message to the given client.
						);
			}
		};
		broker.subscribe(stringSub, String.class); // Register the subscriber with the PSBroker.
		server.waitForEnd(); // Wait for the server to finish executing.
	}
	
}
```

An example client:
```Java
import java.util.Scanner;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.taunet.Client;
import com.joepritzel.taunet.ClientBuilder;
import com.joepritzel.taunet.net.Send;

public class ChatClientExample {

	public static void main(String[] args) throws Exception {
		Client client = new ClientBuilder().build(); // Create a new client.
		Subscriber<String> stringSub = new Subscriber<String>() { // Create a new subscriber, usually defined in its own class.
			@Override
			public void receive(String msg) {
				System.out.println(msg); // Simply print out the string.
			}
		};
		client.getBroker().subscribe(stringSub, String.class); // Register the subscriber to the PSBroker.
		
		System.out.println("What is your name?"); // Ask for a username.
		try (Scanner in = new Scanner(System.in)) {
			String name = in.nextLine(); // Read in username.
			client.start(name); // Start client with the given username.
			while (true) { // Infinitely send messages that are typed in.
				client.getBroker().publish(
						new Send<String>(name + ": " + in.nextLine()));
			}
		}
	}
}
```

<h2 name="license">License</h2>

This project is distributed under the terms of the MIT License. See file "LICENSE" for further information.
