package money.event;

import cn.nukkit.event.Cancellable;
import money.sland.SLand;

/**
 * @author Him188
 */
public class SLandInviteeChangeEvent extends SLandEvent implements Cancellable {
	public static final int TYPE_ADD = 1;
	public static final int TYPE_REMOVE = 2;

	// TODO: 2017/3/31  handler

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
