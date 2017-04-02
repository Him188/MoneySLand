package money;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;

/**
 * @author Him188
 */
public final class EventListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onTouch(PlayerInteractEvent event){
		if (event.getPlayer().getInventory().getItemInHand().getId() == Block.AIR) {
			return;
		}


	}

	// TODO: 2017/4/2
}
