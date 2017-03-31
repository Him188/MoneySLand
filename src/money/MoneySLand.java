package money;

import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import money.generator.SLandGenerator;
import money.sland.LandPool;

public final class MoneySLand extends PluginBase {
	private static MoneySLand instance;
	private LandPool lands;

	private LandPool modifiedLands; // TODO: 2017/3/31  有修改的领地转储

	public static MoneySLand getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		instance = this;
	}

	@Override
	public void onEnable() {
		getDataFolder().mkdir();
		reloadConfig();

		// TODO: 2017/3/31  定义计时器, 保存 modifiedLands 中所有领地数据, 并清空该 pool 

		lands = new LandPool();

		// TODO: 2017/3/31  reload lands from config

		Generator.addGenerator(SLandGenerator.class, "land", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "sland", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "地皮", Generator.TYPE_INFINITE);
	}

	public LandPool getLandPool() {
		return lands;
	}

	// TODO: 2017/3/31  获取地皮 api 
	// TODO: 2017/3/31  获取玩家的所有地皮 api
	// TODO: 2017/3/31  购买地皮
	// TODO: 2017/3/31  出售地皮


	// TODO: 2017/3/31  思考: 地皮的购买方式
}
