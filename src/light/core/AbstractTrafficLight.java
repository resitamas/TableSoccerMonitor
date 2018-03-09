package light.core;

/**
 * @author zutherb
 */
public abstract class AbstractTrafficLight<T> implements TrafficLight {

	public void switchOnAllLeds() {
		for(Color color : Color.values()) {
			switchOn(color);
		}
	}

	public void switchOffAllLeds() {
		for(Color color : Color.values()) {
			switchOff(color);
		}
	}

	protected abstract T map(Color color);
}
