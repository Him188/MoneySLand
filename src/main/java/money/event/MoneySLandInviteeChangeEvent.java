package money.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.sland.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class MoneySLandInviteeChangeEvent extends MoneySLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	public enum Type{
		ADD,
		REMOVE,
	}

	private final String invitee;
	private final Type type;

	public String getInvitee() {
		return invitee;
	}

	public MoneySLandInviteeChangeEvent(SLand land, String invitee, Type type) {
		super(land);
		this.invitee = invitee;
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
