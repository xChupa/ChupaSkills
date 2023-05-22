package chupa.skills.chupaskils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class SkillCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
        	sender.sendMessage(LangOptions.commandforplayers.getMsg(sender));
        	return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("ChupaSkill.skill")) {
        	player.sendMessage(LangOptions.noperm.getMsg(player));
            return true;
        }
        SkillGui.sendMenu(player);
        return true;
    }
}
