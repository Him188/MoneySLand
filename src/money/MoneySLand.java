package money;

import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import money.generator.SLandGenerator;
import money.sland.LandPool;
import money.sland.SLand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MoneySLand extends PluginBase {
	private static MoneySLand instance;
	private LandPool lands;

	private LandPool modifiedLands;

	public static MoneySLand getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		instance = this;

		Generator.addGenerator(SLandGenerator.class, "land", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "sland", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "地皮", Generator.TYPE_INFINITE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		getDataFolder().mkdir();
		reloadConfig();

		lands = new LandPool();
		modifiedLands = new LandPool();

		getConfig().getAll().values().forEach((o) -> lands.add((Map<String, Object>) o));

		Server.getInstance().getScheduler().scheduleRepeatingTask(this, () -> {
			for (SLand land : modifiedLands) {
				Map<String, Object> data = land.save();
				getConfig().set(data.get("time").toString(), data);
			}
			modifiedLands.clear();
		}, 20 * 60 * 2);
	}

	public LandPool getLandPool() {
		return lands;
	}

	public LandPool getModifiedLandPool() {
		return modifiedLands;
	}

	public SLand getLand(Position position) {
		for (SLand land : lands) {
			if (land.inRange(position)) {
				return land;
			}
		}
		return null;
	}

	/**
	 * Gets all of the lands that {@code player} had
	 *
	 * @param player player's name
	 *
	 * @return all of the lands that {@code player} had
	 */
	// TODO: 2017/4/2  test ii
	public SLand[] getLands(String player) {
		List<SLand> list = new ArrayList<>();
		for (SLand land : lands) {
			if (land.getOwner().equalsIgnoreCase(player)) {
				list.add(land);
			}
		}
		return (SLand[]) list.toArray();
	}

	// TODO: 2017/3/31  购买地皮


	// TODO: 2017/3/31  思考: 地皮的购买方式
}
