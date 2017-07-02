package money.tasks;

import cn.nukkit.scheduler.AsyncTask;
import money.sland.SLand;

/**
 * @author Him188 @ MoneySLand Project
 */
public class SLandRegenerateTask extends AsyncTask {
	private final SLand land;
	private final boolean putShopBlock;

	public SLandRegenerateTask(SLand land) {
		this(land, false);
	}

	public SLandRegenerateTask(SLand land, boolean putShopBlock) {
		this.land = land;
		this.putShopBlock = putShopBlock;
	}

	@Override
	public void onRun() {
		this.land.clear();
		this.land.regenerate(putShopBlock);
	}
}
