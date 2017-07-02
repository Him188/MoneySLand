package money.utils;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import money.MoneySLandEventListener;

/**
 * 权限类型
 *
 * @author Him188 @ MoneySLand Project
 */
public enum LevelPermissionType {
	/**
	 * @see PlayerInteractEvent
	 * @see MoneySLandEventListener#permissionChecker(PlayerInteractEvent)
	 */
	BREAK_SHOP("interact", "money.permission.sland.breakshop"),

	/**
	 * @see BlockBreakEvent
	 * @see MoneySLandEventListener#permissionChecker(BlockBreakEvent) 使用处
	 */
	BREAK_AISLE("breakaisle", "money.permission.sland.breakaisle"),

	/**
	 * @see BlockPlaceEvent
	 * @see MoneySLandEventListener#permissionChecker(BlockPlaceEvent) 使用处
	 */
	BREAK_FRAME("breakframe", "money.permission.sland.breakframe");

	private final String stringValue;
	private final String permission;

	LevelPermissionType(String stringValue, String permission) {
		this.stringValue = stringValue;
		this.permission = permission;
	}

	public String stringValue() {
		return stringValue;
	}

	public String getPermission() {
		return permission;
	}

	public boolean testPermission(Player player, Level level) {
		return player.hasPermission(this.permission) || player.hasPermission(this.permission + "." + level.getFolderName());
	}
}
