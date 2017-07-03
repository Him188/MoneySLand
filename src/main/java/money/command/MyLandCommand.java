package money.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import money.MoneySLand;
import money.sland.SLand;
import money.utils.SLandPermissions;

import java.util.HashMap;

/**
 * @author Him188 @ MoneySLand Project
 */
public class MyLandCommand extends SLandCommand implements CommandExecutor {
	public MyLandCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				SLandPermissions.COMMAND_BASE + ";" +
				SLandPermissions.COMMAND_MYLAND + ";" +
				SLandPermissions.COMMAND_MYLAND_OTHERS
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.myland.usage"));
		this.setDescription(owner.translateMessage("commands.myland.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("1arg", new CommandParameter[]{
						new CommandParameter("player name", CommandParameter.ARG_TYPE_RAW_TEXT, true),
				});
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.testPermission(sender)) {
			return true;
		}

		String name;
		switch (args.length) {
			case 0:
				if (!(sender instanceof Player)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.generic.use-in-game"));
					return true;
				}

				name = sender.getName();
				break;
			case 1:
				name = args[1];
				if (!name.equalsIgnoreCase(sender.getName()) && !sender.hasPermission(SLandPermissions.COMMAND_MYLAND_OTHERS)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.myland.no-permission"));
					return true;
				}
				break;
			default:
				return false;
		}

		SLand[] lands = this.getPlugin().getLands(name);
		StringBuilder sb = new StringBuilder(this.getPlugin().translateMessage("commands.myland.list.head",
				"count", lands.length,
				"name", name
		)).append("\n");

		for (SLand land : lands) {
			sb.append(this.getPlugin().translateMessage("commands.myland.list.content",
					"id", land.getId(),
					"owner", land.isOwned() ? land.getOwner() : "无",
					"square", land.getSquare(),
					"level", land.getLevel()
			)).append("\n");
		}

		sender.sendMessage(sb.toString());
		return true;
	}
}
