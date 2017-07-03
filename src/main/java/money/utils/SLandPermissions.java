package money.utils;

import cn.nukkit.Player;

/**
 * @author Him188 @ MoneySLand Project
 */
@SuppressWarnings("SpellCheckingInspection")
public interface SLandPermissions {
	String PERMISSION_BASE = "money.permission.sland";

	String PERMISSION_MODIFY = PERMISSION_BASE + ".modify";
	String PERMISSION_MODIFY_INTERACT = PERMISSION_MODIFY + ".interact";
	String PERMISSION_MODIFY_BREAK = PERMISSION_MODIFY + ".break";
	String PERMISSION_MODIFY_PLACE = PERMISSION_MODIFY + ".place";

	String PERMISSION_BREAK_SHOP = PERMISSION_BASE + ".breakshop";
	String PERMISSION_INTERACT_AISLE = PERMISSION_BASE + ".interactaisle";
	String PERMISSION_BREAK_FRAME = PERMISSION_BASE + ".breakframe";

	String PERMISSION_BUY = PERMISSION_BASE + ".buy";


	String COMMAND_BASE = "money.command.sland";
	String COMMAND_GENERATELAND = COMMAND_BASE + ".generateland";
	String COMMAND_GOTOLAND = COMMAND_BASE + ".gotoland";
	String COMMAND_IDLELAND = COMMAND_BASE + ".idleland";
	String COMMAND_LANDID = COMMAND_BASE + ".landid";
	String COMMAND_SELLLAND = COMMAND_BASE + ".sellland";
	String COMMAND_SELLLAND_OTHERS = COMMAND_SELLLAND + ".others";
	String COMMAND_CLEARLAND = COMMAND_BASE + ".clearland";
	String COMMAND_CLEARLAND_OTHERS = COMMAND_CLEARLAND + ".others";
	String COMMAND_MYLAND = COMMAND_BASE + ".myland";
	String COMMAND_MYLAND_OTHERS = COMMAND_MYLAND + ".others";
	String COMMAND_LANDINVITEE = COMMAND_BASE + ".landinvitee";
	String COMMAND_LANDINVITEE_LIST = COMMAND_LANDINVITEE + ".list";
	String COMMAND_LANDINVITEE_LIST_OTHERS = COMMAND_LANDINVITEE_LIST + ".others";
	String COMMAND_LANDINVITEE_ADD = COMMAND_LANDINVITEE + ".add";
	String COMMAND_LANDINVITEE_ADD_OTHERS = COMMAND_LANDINVITEE_ADD + ".others";
	String COMMAND_LANDINVITEE_REMOVE = COMMAND_LANDINVITEE + ".remove";
	String COMMAND_LANDINVITEE_REMOVE_OTHERS = COMMAND_LANDINVITEE_REMOVE + ".others";

	static boolean testPermission(Player player, String permission, Object child) {
		return player.hasPermission(permission) || player.hasPermission(permission + "." + child);
	}
}
