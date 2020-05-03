package tk.t11e.api.commands;
// Created by booky10 in PaperT11EAPI (20:53 26.02.20)

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.t11e.api.main.Main;

import java.util.List;

public class ClientCrash extends CommandExecutor {


    public ClientCrash() {
        super(Main.main, "clientcrash", "/clientcrash <player>", "clientcrash",
                Receiver.ALL);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.spawnParticle(Particle.EXPLOSION_HUGE, target.getEyeLocation(), Integer.MAX_VALUE);
                sender.sendMessage("Successfully crashed player!");
            } else
                sender.sendMessage("Unknown player!");
        } else
            help(sender);
    }

    @Override
    public void onPlayerExecute(Player player, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (!target.hasPermission("clientcrash.exempt")
                        || player.hasPermission("clientcrash.exempt.bypass")) {
                    target.spawnParticle(Particle.EXPLOSION_HUGE, target.getEyeLocation(), Integer.MAX_VALUE);
                    player.sendMessage(Main.PREFIX + "§aSuccessfully crashed player!");
                } else
                    player.sendMessage(Main.PREFIX + "You can't crash that player!");
            } else
                player.sendMessage(Main.PREFIX + "Unknown player!");
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