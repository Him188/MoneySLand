package money.utils;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import money.SLandEventListener;
import money.sland.SLand;

/**
 * 权限类型
 *
 * @author Him188 @ MoneySLand Project
 * @see SLand#testPermission(Player, PermissionType)
 * @since MoneySLand 1.0.0
 */
public enum PermissionType {
	/**
	 * @see PlayerInteractEvent
	 * @see SLandEventListener#permissionChecker(PlayerInteractEvent)
	 */
	TOUCH("touch", "money.permission.sland.modify.touch"),

	/**
	 * @see BlockBreakEvent
	 * @see SLandEventListener#permissionChecker(BlockBreakEvent) 使用处
	 */
	BREAK("break", "money.permission.sland.modify.break"),

	/**
	 * @see BlockPlaceEvent
	 * @see SLandEventListener#permissionChecker(BlockPlaceEvent) 使用处
	 */
	PLACE("place", "money.permission.sland.modify.place");

	private final String stringValue;
	private final String permission;

	PermissionType(String stringValue, String permission) {
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
