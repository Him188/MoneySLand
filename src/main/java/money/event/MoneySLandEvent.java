package money.event;

import money.sland.SLand;

import java.util.Objects;

/**
 * @author Him188 @ MoneySLand Project
 */
public abstract class MoneySLandEvent extends MoneyPluginEvent {
    public SLand getLand() {
        return this.land;
    }

    private final SLand land;

    public MoneySLandEvent(SLand land) {
	    this.land = Objects.requireNonNull(land);
    }
}
