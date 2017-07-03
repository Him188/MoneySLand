package money.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Position;
import money.MoneySLand;
import money.sland.SLand;
import money.utils.SLandPermissions;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Him188 @ MoneySLand Project
 */
public class LandInviteeCommand extends SLandCommand implements CommandExecutor {
	public LandInviteeCommand(String name, MoneySLand owner) {
		super(name, owner);

		this.setPermission(
				SLandPermissions.COMMAND_BASE + ";" +
				SLandPermissions.COMMAND_LANDINVITEE + ";" +
				SLandPermissions.COMMAND_LANDINVITEE_LIST + ";" +
				SLandPermissions.COMMAND_LANDINVITEE_LIST_OTHERS + ";" +
				SLandPermissions.COMMAND_LANDINVITEE_ADD + ";" +
				SLandPermissions.COMMAND_LANDINVITEE_ADD_OTHERS + ";" +
				SLandPermissions.COMMAND_LANDINVITEE_REMOVE + ";" +
				SLandPermissions.COMMAND_LANDINVITEE_REMOVE_OTHERS
		);
		this.setExecutor(this);
		this.setUsage(owner.translateMessage("commands.landinvitee.usage"));
		this.setDescription(owner.translateMessage("commands.landinvitee.description"));
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("2arg", new CommandParameter[]{
						new CommandParameter("arg", false, new String[]{"list"}),
						new CommandParameter("land id", CommandParameter.ARG_TYPE_INT, true),
				});

				put("3arg", new CommandParameter[]{
						new CommandParameter("arg", false, new String[]{"add", "remove"}),
						new CommandParameter("land id", CommandParameter.ARG_TYPE_INT, false),
						new CommandParameter("player name", CommandParameter.ARG_TYPE_RAW_TEXT, false),
				});
			}
		});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.testPermission(sender)) {
			return true;
		}

		if (args.length < 1) {
			return false;
		}

		SLand land;
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.list.not-player"));
				return true;
			}
			land = this.getPlugin().getLand((Position) sender);
			if (land == null) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.not-found"));
				return true;
			}
		} else {
			int id;
			try {
				id = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.id-invalid",
						"id", args[1]
				));
				return true;
			}

			land = this.getPlugin().getLandPool().get(id);
			if (land == null) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.id-invalid",
						"id", args[1]
				));
				return true;
			}
		}

		if (args[0].equals("list")) {
			if (!sender.hasPermission(SLandPermissions.COMMAND_LANDINVITEE_LIST)) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.list.no-permission"));
				return true;
			}

			if (!land.getOwner().equalsIgnoreCase(sender.getName()) && !sender.hasPermission(SLandPermissions.COMMAND_LANDINVITEE_LIST_OTHERS)) {
				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.list.others.no-permission"));
				return true;
			}

			Set<String> invitees = land.getInvitees();
			StringBuilder sb = new StringBuilder(this.getPlugin().translateMessage("commands.landinvitee.list.head",
					"count", invitees.size()
			)).append("\n");
			for (String s : invitees) {
				sb.append(this.getPlugin().translateMessage("commands.landinvitee.list.content",
						"name", s
				)).append("\n");
			}
			sender.sendMessage(sb.toString());
			return true;
		}

		if (args.length < 2) {
			return false;
		}

		//  /landinvitee add 0 test
		String name = args[2];
		switch (args[0]) {
			case "add":
				if (!sender.hasPermission(SLandPermissions.COMMAND_LANDINVITEE_ADD)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.add.no-permission"));
					return true;
				}

				if (!land.getOwner().equalsIgnoreCase(sender.getName()) && !sender.hasPermission(SLandPermissions.COMMAND_LANDINVITEE_ADD_OTHERS)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.add.others.no-permission"));
					return true;
				}

				if (land.addInvitee(name)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.add.success"));
					return true;
				}

				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.add.failed"));
				return true;
			case "remove":
				if (!sender.hasPermission(SLandPermissions.COMMAND_LANDINVITEE_REMOVE)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.remove.no-permission"));
					return true;
				}

				if (!land.getOwner().equalsIgnoreCase(sender.getName()) && !sender.hasPermission(SLandPermissions.COMMAND_LANDINVITEE_REMOVE_OTHERS)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.remove.others.no-permission"));
					return true;
				}

				if (land.removeInvitee(name)) {
					sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.remove.success"));
					return true;
				}

				sender.sendMessage(this.getPlugin().translateMessage("commands.landinvitee.remove.failed"));
				return true;
		}

		return false;
	}
}
