package money;

import cn.nukkit.utils.ConfigSection;
import money.range.Range;

import java.util.HashSet;
import java.util.Map;

/**
 * Land list
 *
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public final class SLandPool extends HashSet<SLand> {
	public boolean addAndReload(SLand land, ConfigSection data) {
		if (!super.add(land)) {
			return false;
		}

		land.reload(data);
		return true;
	}

	public boolean add(ConfigSection data) {
		Range x, z;
		try {
			x = Range.fromString(data.get("x").toString());
			z = Range.fromString(data.get("z").toString());
		} catch (NullPointerException e) {
			return false;
		}

		return !(x == null || z == null) && addAndReload(new SLand(x, z), data);
	}
}
