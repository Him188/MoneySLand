package money.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import money.MoneySLand;
import money.sland.SLand;

import java.util.HashMap;

/**
 * @author Him188 @ MoneySLand Project
 */
public class GoToLandCommand extends SLandCommand implements CommandExecutor {
	public GoToLandCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				"money.command.sland;" +
				"money.command.sland.gotoland"
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.gotoland.usage"));
		this.setDescription(owner.translateMessage("commands.gotoland.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("1", new CommandParameter[]{
						new CommandParameter("id", CommandParameter.ARG_TYPE_INT, true),
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

		switch (args.length) {
			case 0:
				for (SLand land : this.getPlugin().getLandPool().values()) {
					if (land.getOwner().equals(sender.getName())) {
						((Player) sender).teleport(land.getShopBlock());
						sender.sendMessage(this.getPlugin().translateMessage("commands.gotoland.success"));
						return true;
					}
				}
				sender.sendMessage(this.getPlugin().translateMessage("commands.gotoland.no-anyone"));
				return true;
			case 1:
				int id;
				try {
					id = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.gotoland.id-invalid",
							"id", args[0]
					));
					return true;
				}
				SLand land = this.getPlugin().getLandPool().get(id);
				if (land == null) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.gotoland.id-invalid",
							"id", args[0]
					));
					return true;
				}

				((Player) sender).teleport(land.getShopBlock());
				sender.sendMessage(this.getPlugin().translateMessage("commands.gotoland.success"));
				return true;
			default:
				return false;
		}
	}
}
