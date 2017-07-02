package money.utils;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import money.MoneySLandEventListener;
import money.sland.SLand;

/**
 * 权限类型
 *
 * @author Him188 @ MoneySLand Project
 * @see SLand#testPermission(Player, SLandPermissionType)
 */
public enum SLandPermissionType {
	/**
	 * @see PlayerInteractEvent
	 * @see MoneySLandEventListener#permissionChecker(PlayerInteractEvent)
	 */
	TOUCH("interact", "money.permission.sland.modify.interact"),

	/**
	 * @see BlockBreakEvent
	 * @see MoneySLandEventListener#permissionChecker(BlockBreakEvent) 使用处
	 */
	BREAK("break", "money.permission.sland.modify.break"),

	/**
	 * @see BlockPlaceEvent
	 * @see MoneySLandEventListener#permissionChecker(BlockPlaceEvent) 使用处
	 */
	PLACE("place", "money.permission.sland.modify.place");

	private final String stringValue;
	private final String permission;

	SLandPermissionType(String stringValue, String permission) {
		this.stringValue = stringValue;
		this.permission = permission;
	}

	public String stringValue() {
		return stringValue;
	}

	public String getPermission() {
		return permission;
	}
}
