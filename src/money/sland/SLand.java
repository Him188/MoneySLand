package money.sland;

import cn.nukkit.Player;
import cn.nukkit.Server;
import money.event.SLandInviteeChangeEvent;
import money.range.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// TODO: 2017/3/31 有数据修改的领地列表转储到 LandPool

/**
 * @author Him188
 */
public final class SLand {
	public final Range x;
	public final Range z;

	private String owner;
	private List<String> invitees;
	private long time;

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
	public SLand(Range x, Range z, String owner, List<String> invitees, long time) {
		this(x, z);
		this.owner = owner;
		this.invitees = invitees;
		this.time = time;
	}

	@SuppressWarnings("unchecked")
	public void reload(Map<String, Object> data) {
		this.owner = (String) data.get("owner");
		this.invitees = (List<String>) data.getOrDefault("invitees", new ArrayList<>());
		this.time = (long) data.getOrDefault("time", -1L);
	}

	public Map<String, Object> save() {
		return new HashMap<String, Object>(){
			{
				put("owner", owner);
				put("invitees", invitees);
				put("time", time);
				put("x", x);
				put("z", z);
			}
		};
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