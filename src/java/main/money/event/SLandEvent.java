package money.event;

import cn.nukkit.event.Event;
import money.sland.SLand;

/**
 * @author Him188
 */
public abstract class SLandEvent extends Event {
	public abstract SLand getLand();
}
