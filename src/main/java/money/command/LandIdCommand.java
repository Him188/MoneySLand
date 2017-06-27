package money.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import money.MoneySLand;
import money.sland.SLand;

import java.util.HashMap;

/**
 * @author Him188 @ MoneySLand Project
 */
public class LandIdCommand extends SLandCommand implements CommandExecutor {
	public LandIdCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				"money.command.sland;" +
				"money.command.sland.landid"
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.landid.usage"));
		this.setDescription(owner.translateMessage("commands.landid.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("0", new CommandParameter[]{
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

		SLand land = this.getPlugin().getLand((Position) sender);
		if (land == null) {
			sender.sendMessage(this.getPlugin().translateMessage("commands.landid.failed"));
			return true;
		}

		sender.sendMessage(this.getPlugin().translateMessage("commands.landid.success",
				"id", land.getId(),
				"owner", land.isOwned() ? land.getOwner() : "无"
		));
		return true;
	}
}
