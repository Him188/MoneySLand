package money.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import money.sland.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class MoneySLandBuyEvent extends MoneySLandEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }


    private final Player buyer;
    private float price; //calculated final price

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

