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


	private final Cause cause;
	private final String originalOwner;

	private String newOwner;

	public enum Cause{
		BUY,
		TRANSFER,
		PLUGIN, // for other plugins
		FREE //newOwner = null
	}

	public MoneySLandOwnerChangeEvent(SLand land, String originalOwner, String newOwner, Cause cause) {
		super(land);
		this.originalOwner = originalOwner;
		this.newOwner = newOwner;
		this.cause = cause;
	}

	public Cause getCause() {
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
