package money.sland;

import money.range.Range;

import java.util.HashSet;
import java.util.Map;

/**
 * Land list
 *
 * @author Him188
 */
public final class LandPool extends HashSet<SLand> {
	public boolean addAndReload(SLand land, Map<String, Object> data) {
		if (!super.add(land)) {
			return false;
		}

		land.reload(data);
		return true;
	}

	public boolean add(Map<String, Object> data) {
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
