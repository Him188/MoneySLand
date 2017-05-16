package money.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.math.NukkitRandom;
import money.MoneySLand;
import money.generator.SLandGenerator;

/**
 * @author Him188 @ MoneySLand Project
 * @since MoneySLand 1.0.0
 */
public class GenerateLandCommand extends PluginCommand<MoneySLand> implements CommandExecutor {
    public GenerateLandCommand(String name, MoneySLand owner) {
        super(name, owner);
        this.setPermission("money.command.generateland");
        this.setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(getPlugin().translateMessage("command-generateland-usage"));
            return true;
        }
        if (Server.getInstance().generateLevel(args[0], new java.util.Random().nextLong(), SLandGenerator.class)) {
            sender.sendMessage(getPlugin().translateMessage("command-generateland-success", "level", args[0]));
        } else {
            sender.sendMessage(getPlugin().translateMessage("command-generateland-failed", "level", args[0]));
        }

        return false;
    }
}
