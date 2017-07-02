package money.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.sland.SLand;

/**
 * @author Him188 @ MoneySLand Project
 */
public class MoneySLandClearEvent extends MoneySLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	public MoneySLandClearEvent(SLand land) {
		super(land);
	}
}
