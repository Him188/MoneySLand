package money.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import money.MoneySLand;
import money.generator.SLandGenerator;
import money.utils.SLandPermissions;
import money.utils.SLandUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Him188 @ MoneySLand Project
 */
public class GenerateLandCommand extends SLandCommand implements CommandExecutor {
	public GenerateLandCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				SLandPermissions.COMMAND_BASE + ";" +
				SLandPermissions.COMMAND_GENERATELAND
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.generateland.usage"));
		this.setDescription(owner.translateMessage("commands.generateland.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("2args", new CommandParameter[]{
						new CommandParameter("name", CommandParameter.ARG_TYPE_RAW_TEXT, false),
						new CommandParameter("settings_filename", CommandParameter.ARG_TYPE_RAW_TEXT, true)
				});
			}
		});
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.testPermission(sender)) {
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(getPlugin().translateMessage("commands.generateland.usage"));
			return true;
		}

		Map<String, Object> settings = new HashMap<>();
		String settingsName = "";
		if (args.length == 2) {
			settings = getPlugin().loadGeneratorSettings(args[1]);
			if (settings.isEmpty()) {
				sender.sendMessage(getPlugin().translateMessage("commands.generateland.name-invalid", "level", args[0], "file", args[1]));
				return true;
			}
			settingsName = args[1];
		}

		if (!settingsName.isEmpty()) {
			settings.put("preset", SLandUtils.toPreset(settings));
		}

		if (Server.getInstance().generateLevel(args[0], new java.util.Random().nextLong(), SLandGenerator.class, settings)) {
			sender.sendMessage(getPlugin().translateMessage(settingsName.isEmpty() ? "commands.generateland.success" : "commands.generateland.success.with.settings",
					"level", args[0])
			);
		} else {
			sender.sendMessage(getPlugin().translateMessage("commands.generateland.failed", "level", args[0]));
		}

		return true;
	}
}
