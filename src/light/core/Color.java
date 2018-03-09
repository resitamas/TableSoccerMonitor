package light.core;

/**
 * @author zutherb
 */
public enum Color {
	RED, YELLOW, GREEN;

	public static Color valueOfIgnoreCaseOrNull(String name) {
		for(Color color : Color.values()) {
			if(color.name().equalsIgnoreCase(name)) {
				return color;
			}
		}
		return null;
	}
}
