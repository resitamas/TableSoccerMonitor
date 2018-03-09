package main;

import java.io.IOException;

import light.LightManager;
import light.core.ClewareLightFactory;
import light.core.TrafficLight;
import mock.MockNetwork;
import network.INetwork;
import network.Network;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {

		long refreshTime = 1000;
		INetwork network = new MockNetwork();

		if(args.length == 0) {

		} else if(args.length == 1) {
			refreshTime = Long.parseLong(args[0]);
		} else if(args.length == 2) {
			refreshTime = Long.parseLong(args[0]);
			network = new Network(args[1]);
		}

		try(TrafficLight light = ClewareLightFactory.createNewInstance()) {
			LightManager lightManager = new LightManager(network, light, refreshTime);

			Thread t = new Thread(lightManager);
			t.start();

			System.in.read();

			t.interrupt();
			t.join();
		}

	}

}
