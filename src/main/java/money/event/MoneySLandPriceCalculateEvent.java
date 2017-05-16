package money.event;

import cn.nukkit.Player;
import money.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class MoneySLandPriceCalculateEvent extends MoneySLandEvent {
    private final Player buyer;
    private final float originalPrice;
    private float price;

    public MoneySLandPriceCalculateEvent(SLand land, Player buyer, float price) {
        super(land);
        this.buyer = buyer;
        originalPrice = this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public Player getBuyer() {
        return buyer;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

