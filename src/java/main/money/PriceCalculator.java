package money;

import cn.nukkit.Player;
import cn.nukkit.plugin.Plugin;
import money.sland.SLand;

/**
 * @author Him188
 */
public abstract class PriceCalculator {
	private Plugin plugin;

	public Plugin getPlugin() {
		return plugin;
	}

	public PriceCalculator(Plugin plugin) {
		this.plugin = plugin;
	}

	public abstract double calculate(double originalPrice, Player player, SLand land);
}
