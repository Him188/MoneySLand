package money.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Him188 @ MoneySLand Project
 */
public final class PropertiesHelper {
	private PropertiesHelper() {
		throw new RuntimeException("no PropertiesHelper instances!");
	}

	public static Map<String, Object> loadProperties(Reader reader) {
		Properties properties = new Properties();
		try {
			properties.load(reader);
		} catch (IOException e) {
			return new HashMap<>();
		}
		return new HashMap<String, Object>() {
			{
				properties.forEach((key, value) -> put(key.toString(), value));
			}
		};
	}

	public static Map<String, Object> loadProperties(InputStream stream) {
		Properties properties = new Properties();
		try {
			properties.load(stream);
		} catch (IOException e) {
			return new HashMap<>();
		}
		return new HashMap<String, Object>() {
			{
				properties.forEach((key, value) -> put(key.toString(), value));
			}
		};
	}

	public static Map<String, Object> loadProperties(File file){
		try {
			return loadProperties(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return new HashMap<>();
		}
	}

	public static Map<String, Object> loadProperties(String file){
		return loadProperties(new File(file));
	}
}
