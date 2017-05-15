package money.event;

import money.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public abstract class MoneySLandEvent extends MoneyPluginEvent {
    public SLand getLand() {
        return this.land;
    }

    private final SLand land;

    public MoneySLandEvent(SLand land) {
        this.land = land;
    }
}
// TODO: 2017/5/15 完成事件
