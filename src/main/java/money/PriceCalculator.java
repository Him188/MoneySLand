package money;

import cn.nukkit.Player;
import cn.nukkit.plugin.Plugin;

/**
 * 领地价格计算器. 其他插件可通过添加价格计算器来修改领地价格
 *
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
// TODO: 2017/5/15 更改为事件
@Deprecated
public abstract class PriceCalculator {
	private Plugin plugin;

	public Plugin getPlugin() {
		return plugin;
	}

	public PriceCalculator(Plugin plugin) {
		this.plugin = plugin;
	}

	public abstract float calculate(double originalPrice, Player player, SLand land);
}
