package money;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

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
     * @see SLandEventListener#onTouch(PlayerInteractEvent) 使用处
     */
    TOUCH("touch"),

    /**
     * @see BlockBreakEvent
     * @see SLandEventListener#onBreak(BlockBreakEvent) 使用处
     */
    BREAK("break"),

    /**
     * @see BlockPlaceEvent
     * @see SLandEventListener#onPlace(BlockPlaceEvent) 使用处
     */
    PLACE("place");

    private final String stringValue;

    PermissionType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String stringValue() {
        return stringValue;
    }
}
