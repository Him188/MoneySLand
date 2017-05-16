package money;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.ConfigSection;
import money.event.MoneySLandInviteeChangeEvent;
import money.range.Range;
import money.utils.Utils;

import java.util.*;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public final class SLand {
    public final Range x;
    public final Range z;

    private String level;
    private String owner;
    private List<String> invitees;
    private long time;


    private boolean free;

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    private Vector3 shopBlock;

    public Vector3 getShopBlock() {
        return shopBlock.clone();
    }

    public void setShopBlock(Vector3 shopBlock) {
        this.shopBlock = new Vector3(shopBlock.x, shopBlock.y, shopBlock.z);
    }

    public SLand(Range x, Range z) {
        this.x = x;
        this.z = z;
    }

    @SuppressWarnings("unchecked")
    public void reload(Map<String, Object> data) {
        this.owner = (String) data.get("owner");
        this.invitees = (List<String>) data.getOrDefault("invitees", new ArrayList<>());
        this.time = (long) data.getOrDefault("time", System.currentTimeMillis() + new Random().nextInt());
        this.free = (boolean) data.getOrDefault("free", false);
        this.level = (String) data.getOrDefault("level", "");
        this.shopBlock = Utils.parseVector3((String) data.getOrDefault("shopBlock", "vector3(x=0, y=0, z=0)"));
    }

    public ConfigSection save() {
        return new ConfigSection() {
            {
                put("level", level);
                put("owner", owner);
                put("invitees", invitees);
                put("time", time);
                put("x", x);
                put("free", free);
                put("z", z);
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
     * @return if the {@code position} is included in ths land
     */
    public boolean inRange(Position position) {
        return position.getLevel().getFolderName().equalsIgnoreCase(level)
                && x.inRange(position.getFloorX())
                && z.inRange(position.getFloorZ());
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

    public boolean setOwner(String owner) {
        if (this.isOwned()) {
            return false;
        }
        this.owner = owner;
        MoneySLand.getInstance().getModifiedLandPool().add(this);
        return true;
    }

    public boolean isOwned() {
        return getOwner() != null && !getOwner().equals("");
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
     * @return FALSE on {@link MoneySLandInviteeChangeEvent} is cancelled. TRUE on success or the player is already invited
     */
    public boolean addInvitee(String player) {
        if (this.invitees.contains(player)) {
            return true;
        }

        MoneySLandInviteeChangeEvent event = new MoneySLandInviteeChangeEvent(this, player, MoneySLandInviteeChangeEvent.TYPE_ADD);
        Server.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        MoneySLand.getInstance().getModifiedLandPool().add(this);
        this.invitees.add(player);
        MoneySLand.getInstance().getModifiedLandPool().add(this);
        return true;
    }

    /**
     * Removes a invitee
     *
     * @param player player's name
     * @return FALSE on {@link MoneySLandInviteeChangeEvent} is cancelled. TRUE on success or the player is not invited
     */
    public boolean removeInvitee(String player) {
        if (!this.invitees.contains(player)) {
            return true;
        }

        MoneySLandInviteeChangeEvent event = new MoneySLandInviteeChangeEvent(this, player, MoneySLandInviteeChangeEvent.TYPE_REMOVE);
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
     * @return if {@code player} is invited from this land
     */
    public boolean isInvited(String player) {
        return this.invitees.contains(player);
    }

    /**
     * Returns if the {@code player} can modify this land
     *
     * @param player player
     * @return if the {@code player} can modify this land
     */
    public boolean testPermission(Player player, PermissionType type) {
        return !isFree()
                && getOwner().equalsIgnoreCase(player.getName())
                || this.isInvited(player.getName())
                || player.hasPermission("money.permission.sland." + type.stringValue());
    }
}