package money;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import money.block.SLandShopBlock;
import money.sland.SLand;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Him188
 */
public final class EventListener implements Listener {
	private List<String> temp = new ArrayList<>();

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onTouch(PlayerInteractEvent event) {
		if (event.getItem().getId() == Block.AIR) {
			return;
		}

		Block block = event.getBlock();
		if (block instanceof SLandShopBlock || SLandShopBlock.equals(block)) {
			Item item = event.getItem();
			Player player = event.getPlayer();


			SLand land;
			land = MoneySLand.getInstance().getLand(block);
			if (land == null) {
				return;
			}


			String hash = player.getUniqueId().toString() + block.hashCode();
			switch (item.getId()) {
				case Item.AIR:
					if (!player.hasPermission("money.sland.permission.buy")) {
						player.sendMessage("你没有权限");
						return;
					}

					//buy land
					if (land.isOwned()) {
						block.getLevel().setBlock(block, Block.get(Block.AIR));
						player.sendMessage("这个领地已经被购买了");
						return;
					}

					if (temp.contains(hash)) {
						if (MoneySLand.getInstance().buyLand(land, player)) {
							player.sendMessage("购买成功");
						} else {
							player.sendMessage("购买失败. 资金不足");
						}
					} else {
						temp.add(hash);
						Server.getInstance().getScheduler().scheduleDelayedTask(MoneySLand.getInstance(), () -> temp.remove(hash), 20 * 30);
						player.sendMessage("确认购买该地皮? 购买后不可取消\n地皮价格: " + MoneySLand.getInstance().calculatePrice(player, land) + "\n再次点击该方块确认购买");
					}

				case Item.STICK:
					if (!player.hasPermission("money.sland.permission.free")) {
						player.sendMessage("你没有权限");
						return;
					}

					if (temp.contains(hash)) {
						land.setFree(true);
						if (MoneySLand.getInstance().buyLand(land, player)) {
							player.sendMessage("操作成功");
						} else {
							player.sendMessage("购买失败. 资金不足");
						}
					} else {
						temp.add(hash);
						Server.getInstance().getScheduler().scheduleDelayedTask(MoneySLand.getInstance(), () -> temp.remove(hash), 20 * 30);
						player.sendMessage("确认购买该地皮? 购买后不可取消"+
								"\n地皮价格: " + MoneySLand.getInstance().calculatePrice(player, land) +
								"\n你拥有:   " + MoneySLand.getInstance().getMoney(player) +
								"\n再次点击该方块确认购买");
					}


			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event) {

	}
}
