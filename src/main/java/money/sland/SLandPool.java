package money.sland;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Land list
 *
 * @author Him188 @ MoneySLand Project
 */
public final class SLandPool extends ConcurrentHashMap<Integer, SLand> {
	public void add(SLand land) {
		super.put(land.getId(), land);
	}

	public int nextLandId() {
		int id = size();
		while (this.containsKey(id)) id++;
		return id;
	}
}
