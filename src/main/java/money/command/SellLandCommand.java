package money.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import money.Money;
import money.MoneySLand;
import money.event.MoneySLandOwnerChangeEvent;
import money.sland.SLand;
import money.tasks.SLandRegenerateTask;

import java.util.HashMap;

/**
 * @author Him188 @ MoneySLand Project
 */
public class SellLandCommand extends SLandCommand implements CommandExecutor {
	public SellLandCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				"money.command.sland;" +
				"money.command.sland.sellland;" +
				"money.command.sland.sellland.others"
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.sellland.usage"));
		this.setDescription(owner.translateMessage("commands.sellland.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("1arg", new CommandParameter[]{
						new CommandParameter("land id", CommandParameter.ARG_TYPE_INT, true),
				});
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.testPermission(sender)) {
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(this.getPlugin().translateMessage("commands.generic.use-in-game"));
			return true;
		}

		SLand land;
		switch (args.length) {
			case 0:
				land = this.getPlugin().getLand((Position) sender);
				if (land == null) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.sellland.not-found"));
					return false;
				}
				break;
			case 1:
				int id;
				try {
					id = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.sellland.id-invalid",
							"id", args[0]
					));
					return true;
				}
				land = this.getPlugin().getLandPool().get(id);
				if (land == null) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.sellland.id-invalid",
							"id", args[0]
					));
					return true;
				}
				break;
			default:
				return false;
		}

		MoneySLandOwnerChangeEvent event = new MoneySLandOwnerChangeEvent(land, null, MoneySLandOwnerChangeEvent.Cause.SELL);
		Server.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			sender.sendMessage(this.getPlugin().translateMessage("commands.sellland.failed"));
			return true;
		}
		land.setOwner(event.getNewOwner());
		for (String s : land.getInvitees()) {
			if (!land.removeInvitee(s)) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.sellland.failed"));
				return true;
			}
		}
		Money.getInstance().addMoney(sender.getName(), land.getSellingPrice(), land.getCurrencyType());
		Server.getInstance().getScheduler().scheduleAsyncTask(MoneySLand.getInstance(), new SLandRegenerateTask(land, null, true));
		sender.sendMessage(this.getPlugin().translateMessage("commands.sellland.success",
				"id", land.getId(),
				"amount", land.getSellingPrice(),
				"type", land.getCurrencyType()
		));
		return true;
	}
}
