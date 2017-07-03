package money.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import money.MoneySLand;
import money.sland.SLand;

/**
 * @author Him188 @ MoneySLand Project
 */
public class SLandRegenerateTask extends AsyncTask {
	private final SLand land;
	private final Player player;
	private final boolean putShopBlock;

	public SLandRegenerateTask(SLand land, Player player) {
		this(land, player, false);
	}

	public SLandRegenerateTask(SLand land, Player player, boolean putShopBlock) {
		this.land = land;
		this.player = player;
		this.putShopBlock = putShopBlock;
	}

	@Override
	public void onRun() {
		this.land.clear();
		this.land.regenerate(putShopBlock);
		if (this.player != null) {
			this.player.sendMessage(MoneySLand.getInstance().translateMessage("commands.clearland.finished",
					"id", this.land.getId()
			));
		}
	}
}
