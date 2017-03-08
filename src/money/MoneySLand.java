package money;

import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import money.generator.SLandGenerator;

public class MoneySLand extends PluginBase {
	@Override
	public void onEnable() {
		getDataFolder().mkdir();
		reloadConfig();
		Generator.addGenerator(SLandGenerator.class, "land", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "sland", Generator.TYPE_INFINITE);
		Generator.addGenerator(SLandGenerator.class, "地皮", Generator.TYPE_INFINITE);
	}

	@Override
	public void saveConfig() {
		super.saveConfig();
	}
}
