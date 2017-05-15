package money;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public final class SLandEventListener implements Listener {
    private final MoneySLand plugin;
    private final List<String> temp = new ArrayList<>();

    SLandEventListener(MoneySLand plugin) {
        this.plugin = plugin;
    }

    private static boolean isShopBlock(Block block) {
        try {
            return MoneySLand.getInstance().getLandPool().stream().filter((land) -> land.getShopBlock().equals(block)).count() != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTouch(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(event.getBlock().getLocation().toString());

        Block block = event.getBlock();
        if (isShopBlock(block)) {
            Item item = event.getItem();
            Player player = event.getPlayer();


            SLand land;
            land = this.plugin.getLand(block);
            if (land == null) {
                return;
            }


            String hash = player.getUniqueId().toString() + block.hashCode();

            int id = item.getId();
            switch (id) {
                case Item.AIR:
                case Item.STICK:
                    if (!player.hasPermission("money.permission.sland." + (id == Item.AIR ? "buy" : "free"))) {
                        player.sendMessage(this.plugin.translateMessage("has-no-permission"));
                        return;
                    }

                    //buy land
                    if (land.isOwned()) {
                        block.getLevel().setBlock(block, Block.get(Block.AIR));
                        player.sendMessage(this.plugin.translateMessage("already-bought"));
                        return;
                    }

                    if (temp.remove(hash)) {
                        if (id == Item.STICK) {
                            land.setFree(true);
                        }
                        if (this.plugin.buyLand(land, player)) {
                            player.sendMessage(this.plugin.translateMessage("buy-success"));
                        } else {
                            player.sendMessage(this.plugin.translateMessage("buy-failed"));
                        }
                    } else {
                        temp.add(hash);
                        Server.getInstance().getScheduler().scheduleDelayedTask(this.plugin, () -> temp.remove(hash), 20 * 30);

                        player.sendMessage(this.plugin.translateMessage("buy-if",
                                "price", this.plugin.calculatePrice(player, land),
                                "currency", Money.getInstance().getCurrency1(),
                                "money", this.plugin.getMoney(player))
                        );
                    }
                    break;
                default:
                    player.sendMessage(this.plugin.translateMessage("land-info",
                            "id", land.getTime(),
                            "size", land.getX().getLength() * land.getZ().getLength(),
                            "price", this.plugin.calculatePrice(player, land),
                            "currency", Money.getInstance().getCurrency1(),
                            "money", this.plugin.getMoney(player)) + (event.getPlayer().hasPermission("money.permission.sland.free") ? this.plugin.translateMessage("make-free") : "")
                    );
            }
        }

        if (event.getPlayer().hasPermission("money.permission.sland.modify.interact")) {
            return;
        }

        SLand land = this.plugin.getLand(event.getBlock());
        if (land == null) {
            return;
        }

        if (!land.testPermission(event.getPlayer(), PermissionType.TOUCH)) {
            event.setCancelled();
            event.getPlayer().sendMessage(this.plugin.translateMessage("has-no-permission"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission("money.permission.sland.modify.break")) {
            return;
        }

        SLand land = this.plugin.getLand(event.getBlock());
        if (land == null) {
            return;
        }

        if (!land.testPermission(event.getPlayer(), PermissionType.BREAK)) {
            event.setCancelled();
            event.getPlayer().sendMessage(this.plugin.translateMessage("has-no-permission"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("money.permission.sland.modify.place")) {
            return;
        }

        SLand land = this.plugin.getLand(event.getBlock());
        if (land == null) {
            return;
        }

        if (!land.testPermission(event.getPlayer(), PermissionType.PLACE)) {
            event.setCancelled();
            event.getPlayer().sendMessage(this.plugin.translateMessage("has-no-permission"));
        }
    }
}
