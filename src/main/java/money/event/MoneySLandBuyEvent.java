package money.event;

import cn.nukkit.Player;
import money.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class MoneySLandBuyEvent extends MoneySLandEvent {
    private final Player buyer;
    private float price;

    public MoneySLandBuyEvent(SLand land, Player buyer, float price) {
        super(land);
        this.buyer = buyer;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Player getBuyer() {
        return buyer;
    }
}

