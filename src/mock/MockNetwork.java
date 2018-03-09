package mock;

import java.util.Random;

import network.INetwork;

public class MockNetwork implements INetwork {

	private Random random;

	public MockNetwork() {
		random = new Random();
	}

	@Override
	public String getData() throws Exception {

		int nextNumber = random.nextInt(3);

		if(nextNumber == 0) {
			return "free";
		} else if(nextNumber == 1) {
			throw new Exception("Service unavaliable!");
		} else {
			return "busy";
		}

	}

}
