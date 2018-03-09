package light.core;

import static java.lang.String.format;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;

/**
 * @author zutherb
 */
public final class ClewareLightFactory {

	private ClewareLightFactory() {
		/* NOOP */ }

	private static final Logger LOGGER = LoggerFactory.getLogger(ClewareLightFactory.class);

	private static final int VENDOR_ID = 0xD50; //Cleware Vendor Id
	private static final int PRODUCT_ID = 0x8; //Traffic Light Product Id

	static {
		ClassPathLibraryLoader.loadNativeHIDLibrary();
	}

	public static TrafficLight createNewInstance() {
		try {
			HIDManager hidManager = HIDManager.getInstance();
			HIDDevice hidDevice = hidManager.openById(VENDOR_ID, PRODUCT_ID, null);
			dumpDebugInformation(hidDevice);
			return new ClewareTrafficLightImpl(hidManager, hidDevice);
		} catch(IOException e) {
			throw new TrafficLightException("Traffic Light USB device could not be found.", e);
		}
	}

	private static void dumpDebugInformation(HIDDevice hidDevice) throws IOException {
		LOGGER.debug("Manufacturer:\t{}", hidDevice.getManufacturerString());
		LOGGER.debug("Product:\t\t{}", hidDevice.getProductString());
		LOGGER.debug("SerialNumber:\t{}", hidDevice.getSerialNumberString());
	}

	/**
	 * @author zutherb
	 */
	public static class ClewareTrafficLightImpl extends AbstractTrafficLight<Byte> {

		private static final Logger LOGGER = LoggerFactory.getLogger(ClewareTrafficLightImpl.class);

		private static final byte ZERO = (byte) 0x0;
		private static final byte ONE = (byte) 0x1;

		private static Map<Color, Byte> LED_MAPPING = new HashMap<Color, Byte>() {
			{
				put(Color.RED, (byte) 0x10);
				put(Color.YELLOW, (byte) 0x11);
				put(Color.GREEN, (byte) 0x12);
			}
		};

		private final HIDManager hidManager;
		private final HIDDevice hidDevice;

		ClewareTrafficLightImpl(HIDManager hidManager, HIDDevice hidDevice) {
			this.hidManager = hidManager;
			this.hidDevice = hidDevice;
		}

		public void switchOn(Color color) {
			try {
				byte[] writeBuffer = createSwitchOnSequenceBuffer(color);
				int writtenBytes = hidDevice.write(writeBuffer);
				//				isTrue(writtenBytes == writeBuffer.length, MESSAGE_NOT_ALL_BYTES_FROM_THE_WAS_WRITTEN_TO_USB);
				LOGGER.debug(format(MESSAGE_SWITCH_ON_LED, color.name()));
			} catch(Exception e) {
				LOGGER.error(MESSAGE_SWITCH_ON_ERROR, color.name());
				throw new TrafficLightException(e);
			}
		}

		public void switchOff(Color color) {
			try {
				byte[] writeBuffer = createSwitchOffSequenceBuffer(color);
				int writtenBytes = hidDevice.write(writeBuffer);
				//				isTrue(writtenBytes == writeBuffer.length, MESSAGE_NOT_ALL_BYTES_FROM_THE_WAS_WRITTEN_TO_USB);
				LOGGER.debug(format(MESSAGE_SWITCH_OFF_LED, color.name()));
			} catch(Exception e) {
				LOGGER.error(MESSAGE_SWITCH_OFF_ERROR, color.name());
				throw new TrafficLightException(e);
			}
		}

		private byte[] createSwitchOnSequenceBuffer(Color color) {
			return new byte[] { ZERO, ZERO, map(color), ONE };
		}

		private byte[] createSwitchOffSequenceBuffer(Color color) {
			return new byte[] { ZERO, ZERO, map(color), ZERO };
		}

		public void close() {
			try {
				switchOffAllLeds();
				hidDevice.close();
				hidManager.release();
			} catch(Exception e) {
				LOGGER.error(MESSAGE_USB_DEVICE_COULD_NOT_BE_CLOSED);
				throw new TrafficLightException(e);
			}
		}

		protected Byte map(Color color) {
			return LED_MAPPING.get(color);
		}
	}
}
