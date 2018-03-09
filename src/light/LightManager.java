package light;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import light.core.Color;
import light.core.TrafficLight;
import network.INetwork;

public class LightManager implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightManager.class);

	private long sleepingMS;
	private INetwork network;
	private TrafficLight trafficLight;

	Color activeColor;

	public LightManager(INetwork network, TrafficLight trafficLight, long sleepingMS) {
		this.network = network;
		this.trafficLight = trafficLight;
		this.sleepingMS = sleepingMS;
	}

	@Override
	public void run() {

		try {
			while(!Thread.interrupted()) {
				try {
					switchLed();
				} catch(Exception e) {
					switchToColor(Color.YELLOW);
					LOGGER.error(e.getMessage(), e);
				}
				Thread.sleep(sleepingMS);
			}
		} catch(InterruptedException iex) {

		}

	}

	private void switchLed() throws Exception {

		if(network.getData().equals("free")) {
			switchToColor(Color.GREEN);
		} else {
			switchToColor(Color.RED);
		}
	}

	private void switchToColor(Color color) {

		if(color != activeColor) {
			trafficLight.switchOffAllLeds();
			trafficLight.switchOn(color);
			activeColor = color;
			LOGGER.info("Color is: " + color.toString());
		}
	}

}
