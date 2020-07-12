package tk.t11e.api.commands;
// Created by booky10 in PaperT11EAPI (13:39 28.04.20)

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.t11e.api.main.PaperT11EAPIMain;
import tk.t11e.api.util.AttributeHelper;

import java.util.List;

public class SetMaxHealth extends CommandExecutor {


    public SetMaxHealth() {
        super(PaperT11EAPIMain.main, "maxhealth", "/maxhealth <player> <health>",
                "api.maxhealth", Receiver.ALL, "setmaxhealth", "smh", "mh");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                double health;
                try {
                    health = Double.parseDouble(args[1]);
                } catch (NumberFormatException exception) {
                    if (args[1].equalsIgnoreCase("reset")
                            || args[1].equalsIgnoreCase("normal"))
                        health = 20D;
                    else {
                        sender.sendMessage("<health> must be a number!");
                        return;
                    }
                }
                AttributeHelper.setMaxHealth(target, health);
                sender.sendMessage("The max health has been modified!");
            } else
                sender.sendMessage("Unknown player!");
        } else
            help(sender);
    }

    @Override
    public void onPlayerExecute(Player player, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                double health;
                try {
                    health = Double.parseDouble(args[1]);
                } catch (NumberFormatException exception) {
                    if (args[1].equalsIgnoreCase("reset")
                            || args[1].equalsIgnoreCase("normal"))
                        health = 20D;
                    else {
                        player.sendMessage(PaperT11EAPIMain.PREFIX + "<health> must be a number!");
                        return;
                    }
                }
                AttributeHelper.setMaxHealth(target, health);
                player.sendMessage(PaperT11EAPIMain.PREFIX + "§aThe max health has been modified!");
            } else
                player.sendMessage(PaperT11EAPIMain.PREFIX + "Unknown player!");
        } else
            help(player);
    }

    @Override
    public List<String> onComplete(CommandSender sender, String[] args, List<String> completions) {
        if (args.length == 1)
            completions.addAll(getOnlinePlayerNames());
        return completions;
    }
}