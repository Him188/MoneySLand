package money.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import money.MoneySLand;
import money.sland.SLand;

import java.util.HashMap;
import java.util.stream.Stream;

/**
 * @author Him188 @ MoneySLand Project
 */
public class IdleLandCommand extends SLandCommand implements CommandExecutor {
	public IdleLandCommand(String name, MoneySLand owner) {
		super(name, owner);


		this.setPermission(
				"money.command.sland;" +
				"money.command.sland.idleland"
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.idleland.usage"));
		this.setDescription(owner.translateMessage("commands.idleland.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("1arg", new CommandParameter[]{
						new CommandParameter("level", CommandParameter.ARG_TYPE_RAW_TEXT, true),
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

		Level level;
		switch (args.length) {
			case 0:
				level = ((Player) sender).getLevel();
				break;
			case 1:
				StringBuilder levelName = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					levelName.append(args[i]);
				}

				level = Server.getInstance().getLevelByName(levelName.toString());
				if (level == null) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.idleland.level-invalid"));
					return true;
				}
				break;
			default:
				return false;
		}


		Stream<SLand> values = this.getPlugin().getLandPool().values().stream().filter(land -> land.getLevel().equals(level.getFolderName()) && !land.isOwned());
		SLand land;
		if (((land = values.findFirst().orElse(null)) == null)) {
			sender.sendMessage(this.getPlugin().translateMessage("commands.idleland.full"));
			return true;
		}

		((Player) sender).teleport(land.getShopBlock().add(0, 2));
		sender.sendMessage(this.getPlugin().translateMessage("commands.idleland.success"));
		return true;
	}
}
