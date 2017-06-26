package money.sland;

import cn.nukkit.utils.ConfigSection;

import java.util.HashMap;
import java.util.Objects;

/**
 * Land list
 *
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public final class SLandPool extends HashMap<Integer, SLand> {
	public void add(SLand land) {
		super.put(land.getId(), land);
	}

	public boolean add(ConfigSection data) {
		Objects.requireNonNull(data, "argument data must not be null");

		try {
			this.add(SLand.newLand(data));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int nextLandId(){
		int id = 0;
		while (this.containsKey(id)) id++;
		return id;
	}
}
