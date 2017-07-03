package money.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import money.MoneySLand;
import money.event.MoneySLandClearEvent;
import money.sland.SLand;
import money.tasks.SLandRegenerateTask;
import money.utils.SLandPermissions;

import java.util.HashMap;

/**
 * @author Him188 @ MoneySLand Project
 */
public class ClearLandCommand extends SLandCommand implements CommandExecutor {
	public ClearLandCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				SLandPermissions.COMMAND_BASE + ";" +
				SLandPermissions.COMMAND_CLEARLAND + ";" +
				SLandPermissions.COMMAND_CLEARLAND_OTHERS
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.clearland.usage"));
		this.setDescription(owner.translateMessage("commands.clearland.description"));
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
					sender.sendMessage(this.getPlugin().translateMessage("commands.clearland.not-found"));
					return false;
				}
				break;
			case 1:
				int id;
				try {
					id = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.clearland.id-invalid",
							"id", args[0]
					));
					return true;
				}
				land = this.getPlugin().getLandPool().get(id);
				if (land == null) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.clearland.id-invalid",
							"id", args[0]
					));
					return true;
				}

				if (!sender.getName().equalsIgnoreCase(land.getOwner()) && sender.hasPermission(SLandPermissions.COMMAND_CLEARLAND_OTHERS)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.clearland.no-permission"));
					return true;
				}
				break;
			default:
				return false;
		}

		MoneySLandClearEvent event = new MoneySLandClearEvent(land);
		Server.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			sender.sendMessage(this.getPlugin().translateMessage("commands.clearland.failed"));
			return true;
		}
		Server.getInstance().getScheduler().scheduleAsyncTask(MoneySLand.getInstance(), new SLandRegenerateTask(land, (Player) sender));
		sender.sendMessage(this.getPlugin().translateMessage("commands.clearland.success",
				"id", land.getId()
		));
		return true;
	}
}
