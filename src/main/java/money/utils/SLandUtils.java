package money.utils;

import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * @author Him188 @ MoneySLand Project
 */
public final class SLandUtils {
	private SLandUtils() {
		throw new RuntimeException("no SLandUtils instances!");
	}

	/**
	 * Load a properties file, using UTF-8 charset.
	 *
	 * @param stream stream
	 *
	 * @return properties map
	 */
	public static Map<String, Object> loadProperties(InputStream stream) {
		Properties properties = new Properties();
		try {
			properties.load(new InputStreamReader(stream, "UTF-8"));
		} catch (IOException e) {
			return new HashMap<>();
		}
		return new HashMap<String, Object>() {
			{
				properties.forEach((key, value) -> put(key.toString(), value));
			}
		};
	}

	/**
	 * Load a properties file, using UTF-8 charset.
	 *
	 * @param file file
	 *
	 * @return properties map
	 */
	public static Map<String, Object> loadProperties(File file) {
		try {
			return loadProperties(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return new HashMap<>();
		}
	}

	/**
	 * Load a properties file, using UTF-8 charset.
	 *
	 * @param file file
	 *
	 * @return properties map
	 */
	public static Map<String, Object> loadProperties(String file) {
		return loadProperties(new File(file));
	}

	public static Vector3 parseVector3(String arg) {
		arg += ",";
		if (arg.startsWith("Vector3")) {
			arg = arg.substring(0, arg.length() - "Vector3".length());
			String[] args = arg.split("=");
			if (args.length == 4) {
				return new Vector3(
						Double.parseDouble(args[1].substring(0, args[1].length() - 2)),
						Double.parseDouble(args[2].substring(1, args[2].length() - 2)),
						Double.parseDouble(args[3].substring(2, args[3].length() - 2))
				);
			} else return null;
		}
		return null;
	}

	public static <T> boolean arrayContains(T[] arr, T item) {
		for (T t : arr) {
			if (t.equals(item)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Vector2> T calculateCenterPos(T v1, T v2) {
		return (T) v1.add(v1.subtract(v2).divide(2));
	}

	public static Vector2 calculateCenterPos(Range x, Range z) {
		return calculateCenterPos(new Vector2(x.getMax(), z.getMax()), new Vector2(x.getMax(), z.getMax()));
	}
}
