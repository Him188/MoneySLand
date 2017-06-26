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
import cn.nukkit.level.Position;
import money.sland.SLand;
import money.utils.PermissionType;
import money.utils.StringAligner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			return MoneySLand.getInstance().getLandPool().values().stream().filter((land) -> land.getShopBlock().equals(block)).count() != 0;
		} catch (Exception e) {
			return false;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void permissionChecker(PlayerInteractEvent event) {
		if ((event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		    && !this.testPermission(event.getPlayer(), event.getBlock(), PermissionType.TOUCH)) {
			event.setCancelled();
			event.getPlayer().sendMessage(this.plugin.translateMessage("has-no-permission"));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void permissionChecker(BlockBreakEvent event) {
		if (!this.testPermission(event.getPlayer(), event.getBlock(), PermissionType.BREAK)) {
			event.setCancelled();
			event.getPlayer().sendMessage(this.plugin.translateMessage("has-no-permission"));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void permissionChecker(BlockPlaceEvent event) {
		if (!this.testPermission(event.getPlayer(), event.getBlock(), PermissionType.PLACE)) {
			event.setCancelled();
			event.getPlayer().sendMessage(this.plugin.translateMessage("has-no-permission"));
		}
	}

	private boolean testPermission(Player player, Position position, PermissionType type) {
		return player.hasPermission(type.getPermission()) || Optional.ofNullable(this.plugin.getLand(position)).map((land) -> land.testPermission(player,
				type)).orElse(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void shopActionListener(PlayerInteractEvent event) {
		Block block = event.getBlock();
		if (!isShopBlock(block)) {
			return;
		}

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
					StringAligner aligner = new StringAligner(this.plugin.calculatePrice(player, land), Money.getInstance().getMoney(player));

					player.sendMessage(this.plugin.translateMessage("buy-if",
							"price", aligner.string(),
							"currency", Money.getInstance().getCurrency1(),
							"money", aligner.another())
					);
				}
				break;
			default:
				StringAligner aligner = new StringAligner(this.plugin.calculatePrice(player, land), Money.getInstance().getMoney(player));

				player.sendMessage(this.plugin.translateMessage("land-info",
						"id", land.getTime(),
						"size", land.getX().getRealLength() * land.getZ().getRealLength(),
						"price", aligner.string(),
						"currency", Money.getInstance().getCurrency1(),
						"money", aligner.another()) + (event.getPlayer().hasPermission("money.permission.sland.free") ? this.plugin.translateMessage("make-free") : "")
				);
		}

	}
}
