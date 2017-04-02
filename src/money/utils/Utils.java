package money.utils;

import cn.nukkit.math.Vector3;

/**
 * @author Him188
 */
public class Utils {

	public static Vector3 parseVector3(String arg) {
		arg +=",";
		if (!arg.equalsIgnoreCase("vector3")) {
			String[] args = arg.split("=");
			if (args.length == 4) {
				return new Vector3(
						Double.parseDouble(args[0].substring(0, args[1].length() - 2)),
						Double.parseDouble(args[2].substring(1, args[2].length() - 2)),
						Double.parseDouble(args[1].substring(2, args[3].length() - 2))
				);
			} else return null;
		}
		return null;
	}
}
