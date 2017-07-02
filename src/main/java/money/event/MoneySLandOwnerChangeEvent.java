package money.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.sland.SLand;

import java.util.Objects;

/**
 * @author Him188 @ MoneySLand Project
 */
public class MoneySLandOwnerChangeEvent extends MoneySLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	private final Cause cause;

	private Player newOwner;

	public enum Cause{
		BUY,
		TRANSFER,
		SELL,
		PLUGIN, // for other plugins
	}

	public MoneySLandOwnerChangeEvent(SLand land, Player newOwner, Cause cause) {
		super(land);
		this.newOwner = newOwner; //nullable
		this.cause = Objects.requireNonNull(cause);
	}

	public Cause getCause() {
		return cause;
	}

	public Player getNewOwner() {
		return newOwner;
	}

	public void setNewOwner(Player newOwner) {
		this.newOwner = newOwner;
	}
}
