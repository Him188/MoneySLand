package money.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import money.MoneySLand;
import money.sland.SLand;
import money.utils.SLandUtils;

import java.util.HashMap;

/**
 * @author Him188 @ MoneySLand Project
 */
public class MyLandCommand extends SLandCommand implements CommandExecutor {
	public MyLandCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				"money.command.sland;" +
				"money.command.sland.myland;" +
				"money.command.sland.myland.others"
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.myland.usage"));
		this.setDescription(owner.translateMessage("commands.myland.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("1arg", new CommandParameter[]{
						new CommandParameter("player name", CommandParameter.ARG_TYPE_RAW_TEXT),
				});

				put("1arg_", new CommandParameter[]{
						new CommandParameter("player", CommandParameter.ARG_TYPE_PLAYER),
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
				name = SLandUtils.arrayMerge(args, 1, args.length - 1);
				if (!name.equalsIgnoreCase(sender.getName()) && !sender.hasPermission("money.command.sland.myland.others")) {
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
		));

		for (SLand land : lands) {
			sb.append(this.getPlugin().translateMessage("commands.myland.list.content",
					"id", land.getId(),
					"owner", land.isOwned() ? land.getOwner() : "无",
					"square", land.getSquare(),
					"level", land.getLevel()
			));
		}

		sender.sendMessage(sb.toString());
		return true;
	}
}
