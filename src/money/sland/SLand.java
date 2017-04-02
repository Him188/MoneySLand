package money.sland;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import money.MoneySLand;
import money.event.SLandInviteeChangeEvent;
import money.range.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Him188
 */
public final class SLand {
	public final Range x;
	public final Range z;

	private String level;
	private String owner;
	private List<String> invitees;
	private long time;


	private Vector3 shopBlock;

	/**
	 * Be used when reloading land from disk
	 */
	public SLand(Range x, Range z) {
		this.x = x;
		this.z = z;
	}

	/**
	 * Only be used when buying a land
	 */
	// TODO: 2017/3/31 购买后不构建地皮, 而是调用 reload 设置值
	@Deprecated
	public SLand(Range x, Range z, String owner, List<String> invitees, long time, String level) {
		this(x, z);
		this.owner = owner;
		this.invitees = invitees;
		this.time = time;
		this.level = level;
	}

	@SuppressWarnings("unchecked")
	public void reload(Map<String, Object> data) {
		this.owner = (String) data.get("owner");
		this.invitees = (List<String>) data.getOrDefault("invitees", new ArrayList<>());
		this.time = (long) data.getOrDefault("time", -1L);
		this.level = (String) data.getOrDefault("level", "");


		// TODO: 2017/4/2 shopBlock的保存和读取
		//this.shopBlock =
	}

	public Map<String, Object> save() {
		return new HashMap<String, Object>() {
			{
				put("level", level);
				put("owner", owner);
				put("invitees", invitees);
				put("time", time);
				put("x", x);
				put("z", z);
			}
		};
	}

	/**
	 * Gets the level folder name
	 *
	 * @return the level folder name
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Returns if the {@code position} is included in ths land
	 *
	 * @param position position
	 * @return if the {@code position} is included in ths land
	 */
	public boolean inRange(Position position){
		return position.getLevel().getFolderName().equalsIgnoreCase(level) && x.inRange(position.getFloorX()) && z.inRange(position.getFloorZ());
	}

	/**
	 * Gets the time when buying the land (in milliseconds ({@code System.currentTimeMillis()}))
	 *
	 * @return the time when buying the land
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Gets the x range
	 *
	 * @return the x range
	 */
	public Range getX() {
		return x;
	}

	/**
	 * Gets the z range
	 *
	 * @return the z range
	 */
	public Range getZ() {
		return z;
	}

	/**
	 * Gets the owner's name
	 *
	 * @return the owner's name
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Returns the copy of invitees
	 *
	 * @return the copy of invitees
	 */
	public List<String> getInvitees() {
		return new ArrayList<>(invitees);
	}

	/**
	 * Adds a invitee
	 *
	 * @param player player's name
	 *
	 * @return FALSE on {@link SLandInviteeChangeEvent} is cancelled. TRUE on success or the player is already invited
	 */
	public boolean addInvitee(String player) {
		if (this.invitees.contains(player)) {
			return true;
		}

		SLandInviteeChangeEvent event = new SLandInviteeChangeEvent(this, player, SLandInviteeChangeEvent.TYPE_ADD);
		Server.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		MoneySLand.getInstance().getModifiedLandPool().add(this);
		this.invitees.add(player);
		return true;
	}

	/**
	 * Removes a invitee
	 *
	 * @param player player's name
	 *
	 * @return FALSE on {@link SLandInviteeChangeEvent} is cancelled. TRUE on success or the player is not invited
	 */
	public boolean removeInvitee(String player) {
		if (!this.invitees.contains(player)) {
			return true;
		}

		SLandInviteeChangeEvent event = new SLandInviteeChangeEvent(this, player, SLandInviteeChangeEvent.TYPE_REMOVE);
		Server.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		this.invitees.remove(player);
		return true;
	}

	/**
	 * Returns if {@code player} is invited from this land
	 *
	 * @param player player's name
	 *
	 * @return if {@code player} is invited from this land
	 */
	public boolean isInvited(String player) {
		return this.invitees.contains(player);
	}

	/**
	 * Returns if the {@code player} can modify this land
	 *
	 * @param player player
	 *
	 * @return if the {@code player} can modify this land
	 */
	public boolean testPermission(Player player) {
		return getOwner().equalsIgnoreCase(player.getName()) || this.isInvited(player.getName()) || player.hasPermission("money.sland.permission.modify");
	}
}