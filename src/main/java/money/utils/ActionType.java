package money.utils;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import money.MoneySLandEventListener;
import money.sland.SLand;

/**
 * @author Him188 @ MoneySLand Project
 * @see SLand#testPermission(Player, ActionType)
 */
public enum ActionType {
	/**
	 * @see PlayerInteractEvent
	 * @see MoneySLandEventListener#permissionChecker(PlayerInteractEvent)
	 */
	TOUCH("interact", SLandPermissions.PERMISSION_MODIFY_INTERACT),

	/**
	 * @see BlockBreakEvent
	 * @see MoneySLandEventListener#permissionChecker(BlockBreakEvent)
	 */
	BREAK("break", SLandPermissions.PERMISSION_MODIFY_BREAK),

	/**
	 * @see BlockPlaceEvent
	 * @see MoneySLandEventListener#permissionChecker(BlockPlaceEvent)
	 */
	PLACE("place", SLandPermissions.PERMISSION_MODIFY_PLACE);

	private final String stringValue;
	private final String permission;

	ActionType(String stringValue, String permission) {
		this.stringValue = stringValue;
		this.permission = permission;
	}

	public boolean testPermission(Player player, SLand land) {
		return player.hasPermission(this.permission) || player.hasPermission(this.stringValue + "." + land.getId());
	}
}
