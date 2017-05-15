package money.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class MoneySLandInviteeChangeEvent extends MoneySLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	public static final int TYPE_ADD = 1;
	public static final int TYPE_REMOVE = 2;

	private final String invitee;
	private final int type;

	public String getInvitee() {
		return invitee;
	}

	public MoneySLandInviteeChangeEvent(SLand land, String invitee, int type) {
		super(land);
		this.invitee = invitee;
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
