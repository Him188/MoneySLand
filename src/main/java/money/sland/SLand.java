package money.sland;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.ConfigSection;
import money.MoneySLand;
import money.event.MoneySLandInviteeChangeEvent;
import money.utils.PermissionType;
import money.utils.Range;
import money.utils.SLandUtils;

import java.util.*;

/**
 * @author Him188 @ MoneySLand Project
 */
public final class SLand {
	public static SLand newLand(int id, Range x, Range z, String owner, Collection<String> invitees, long time, boolean free, String level, Vector3 shopBlock) {
		return new SLand(id, x, z, owner, invitees, time, free, level, shopBlock);
	}

	public static SLand newLand(int id, Range x, Range z, Player owner, Collection<String> invitees, long time, boolean free, String level, Vector3 shopBlock) {
		return new SLand(id, x, z, owner.getName(), invitees, time, free, level, shopBlock);
	}

	public static SLand newLand(int id, Range x, Range z, String owner, Collection<String> invitees, long time, boolean free, Level level, Vector3 shopBlock) {
		return new SLand(id, x, z, owner, invitees, time, free, level.getFolderName(), shopBlock);
	}

	public static SLand newLand(int id, Range x, Range z, Player owner, Collection<String> invitees, long time, boolean free, Level level, Vector3 shopBlock) {
		return new SLand(id, x, z, owner.getName(), invitees, time, free, level.getName(), shopBlock);
	}

	public static SLand newLand(ConfigSection data) throws IllegalArgumentException, NullPointerException {
		return new SLand(
				data.getInt("id", -1),
				Range.fromString(data.getString("x", null)),
				Range.fromString(data.getString("z", null)),
				data.getString("owner", null),
				data.containsKey("invitees") ? data.getStringList("invitees") : null,
				data.getLong("time", -1),
				data.getBoolean("free"),
				data.getString("level", null),
				SLandUtils.parseVector3(data.getString("shopBlock", null))
		);
	}

	public static SLand newInitialLand(int id, Range x, Range z, String level, Vector3 shopBLock) {
		return new SLand(id, x, z, "", new ArrayList<>(), System.currentTimeMillis(), false, level, shopBLock);
	}

	public static SLand newInitialLand(int id, Range x, Range z, Level level, Vector3 shopBLock) {
		return new SLand(id, x, z, "", new ArrayList<>(), System.currentTimeMillis(), false, level.getFolderName(), shopBLock);
	}


	private final Range x;
	private final Range z;
	private final int id;

	private final String level;
	private String owner;
	private final LinkedHashSet<String> invitees;
	private final long time;

	private final Vector3 shopBlock;

	private SLand(int id, Range x, Range z, String owner, Collection<String> invitees, long time, boolean free, String level, Vector3 shopBlock) {
		Objects.requireNonNull(x);
		Objects.requireNonNull(z);
		Objects.requireNonNull(owner);
		Objects.requireNonNull(invitees);
		Objects.requireNonNull(level);
		Objects.requireNonNull(shopBlock);

		if (id < 0) {
			throw new IllegalArgumentException("sland id is invalid");
		}

		if (time < 0) {
			throw new IllegalArgumentException("sland time is invalid");
		}

		this.x = x;
		this.z = z;
		this.id = id;
		this.owner = owner;
		this.invitees = new LinkedHashSet<>(invitees);
		this.time = time;
		this.level = level;
		this.shopBlock = shopBlock;
	}

	public int getId() {
		return id;
	}


	public Vector3 getShopBlock() {
		return shopBlock.clone();
	}

	public ConfigSection save() {
		return new ConfigSection() {
			{
				put("id", id);
				put("level", level);
				put("owner", owner);
				put("invitees", new ArrayList<>(invitees));
				put("time", time);
				put("x", x.toString());
				put("z", z.toString());
				put("shopBlock", shopBlock.toString());
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
	 *
	 * @return TRUE on the {@code position} is included in ths land, otherwise FALSE
	 */
	public boolean inRange(Position position) {
		return position.getLevel().getFolderName().equalsIgnoreCase(level)
		       && x.realInRange(position.getFloorX())
		       && z.realInRange(position.getFloorZ());
	}

	/**
	 * Calculate the square of this land
	 *
	 * @return square in square blocks
	 */
	public int getSquare() {
		return x.getRealLength() * z.getRealLength();
	}

	/**
	 * Gets the time when generating the land (in milliseconds ({@link System#currentTimeMillis()}))
	 *
	 * @return the time when generating the land in milliseconds
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
	 * Sets the owner's name
	 *
	 * @param owner the owner's name
	 *
	 * @return TRUE always
	 */
	public boolean setOwner(String owner) {
		//if (this.isOwned()) {
		//	return false;
		//}
		this.owner = owner;
		MoneySLand.getInstance().getModifiedLandPool().add(this);
		return true;
	}

	/**
	 * Checks if this land has owner
	 *
	 * @return if this land has owner
	 */
	public boolean isOwned() {
		return getOwner() != null && !getOwner().isEmpty();
	}

	/**
	 * Returns {@link Collections.UnmodifiableSet} of invitees
	 *
	 * @return {@link Collections.UnmodifiableSet} of invitees
	 */
	public Set<String> getInvitees() {
		return Collections.unmodifiableSet(invitees);
	}

	/**
	 * Adds an invitee
	 *
	 * @param player player's name
	 *
	 * @return TRUE on success or the player is already invited. FALSE on {@link MoneySLandInviteeChangeEvent} is cancelled
	 */
	public boolean addInvitee(String player) {
		if (this.invitees.contains(player)) {
			return true;
		}

		MoneySLandInviteeChangeEvent event = new MoneySLandInviteeChangeEvent(this, player, MoneySLandInviteeChangeEvent.Type.ADD);
		Server.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		this.invitees.add(player);
		MoneySLand.getInstance().getModifiedLandPool().add(this);
		return true;
	}

	/**
	 * Removes an invitee
	 *
	 * @param player player's name
	 *
	 * @return TRUE on success or <code>player</code> is not invited. FALSE on {@link MoneySLandInviteeChangeEvent} is cancelled
	 */
	public boolean removeInvitee(String player) {
		if (!this.invitees.contains(player)) {
			return true;
		}

		MoneySLandInviteeChangeEvent event = new MoneySLandInviteeChangeEvent(this, player, MoneySLandInviteeChangeEvent.Type.REMOVE);
		Server.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		this.invitees.remove(player);
		MoneySLand.getInstance().getModifiedLandPool().add(this);
		return true;
	}

	/**
	 * Returns if {@code player} is invited from this land
	 *
	 * @param player player's name
	 *
	 * @return TRUE on {@code player} is invited from this land, otherwise FALSE
	 */
	public boolean isInvited(String player) {
		return this.invitees.contains(player);
	}

	/**
	 * Returns if the {@code player} can modify this land
	 *
	 * @param player player
	 *
	 * @return TRUE on {@code player} can modify this land, otherwise FALSE
	 */
	public boolean testPermission(Player player, PermissionType type) {
		return getOwner().equalsIgnoreCase(player.getName())
		       || this.isInvited(player.getName())
		       || player.hasPermission("money.permission.sland." + this.getId() + "." + type.stringValue());
	}

}