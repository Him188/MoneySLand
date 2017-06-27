package money.utils;

import cn.nukkit.math.Vector3;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

	public static Vector3 parseVector3(final String arg) {
		char[] chars = arg.toCharArray();

		double[] v = new double[3];
		int vPos = 0;
		char[] buff = new char[64];
		int buffPos = 0;
		boolean readingPos = false;
		for (char aChar : chars) {
			if (aChar == '=') {
				readingPos = true;
				continue;
			}

			if (readingPos) {
				if (aChar == ',' || aChar == ')') {
					v[vPos++] = Double.parseDouble(new String(buff));
					buff = new char[64];
					if (aChar == ')') {
						return new Vector3(v[0], v[1], v[2]);
					}
					readingPos = false;
				} else {
					buff[buffPos++] = aChar;
				}
			}
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
}
