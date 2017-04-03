package money.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.sland.SLand;

/**
 * @author Him188
 */
public class SLandInviteeChangeEvent extends SLandEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() {
		return handlers;
	}


	public static final int TYPE_ADD = 1;
	public static final int TYPE_REMOVE = 2;

	public final SLand land;
	public final String invitee;

	public final int type;

	public SLand getLand() {
		return land;
	}

	public String getInvitee() {
		return invitee;
	}

	public SLandInviteeChangeEvent(SLand land, String invitee, int type) {
		this.land = land;
		this.invitee = invitee;
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
