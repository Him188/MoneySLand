package money.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class MoneySLandOwnerChangeEvent extends MoneySLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	private final int cause;
	private final String originalOwner;

	private String newOwner;

	public static final int CAUSE_BUY = 1;
	public static final int CAUSE_TRANSFER = 2;
	public static final int CAUSE_PLUGIN = 3; // for other plugins
	public static final int CAUSE_FREE = 4; //newOwner = null


	public MoneySLandOwnerChangeEvent(SLand land, String originalOwner, String newOwner, int cause) {
		super(land);
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
