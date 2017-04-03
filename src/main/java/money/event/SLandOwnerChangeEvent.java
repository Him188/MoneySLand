package money.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.sland.SLand;

/**
 * @author Him188
 */
public class SLandOwnerChangeEvent extends SLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	private SLand land;

	public final String originalOwner;
	public String newOwner;

	public static final int CAUSE_BUY = 1;
	public static final int CAUSE_TRANSFER = 2;
	public static final int CAUSE_PLUGIN = 3; // for other plugins
	public static final int CAUSE_FREE = 4; //newOwner = null

	public final int cause;


	public SLand getLand() {
		return land;
	}

	public SLandOwnerChangeEvent(SLand land, String originalOwner, String newOwner, int cause) {
		this.land = land;
		this.originalOwner = originalOwner;
		this.newOwner = newOwner;
		this.cause = cause;
	}

	public int getCause() {
		return cause;
	}

	public String getNewOwner() {
		return newOwner;
	}

	public String getOriginalOwner() {
		return originalOwner;
	}

	public void setNewOwner(String newOwner) {
		this.newOwner = newOwner;
	}
}
