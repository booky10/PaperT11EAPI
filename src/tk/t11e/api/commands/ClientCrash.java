package tk.t11e.api.commands;
// Created by booky10 in PaperT11EAPI (20:53 26.02.20)

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.t11e.api.main.PaperT11EAPIMain;
import tk.t11e.api.util.VersionHelper;

import java.util.List;

public class ClientCrash extends CommandExecutor {


    public ClientCrash() {
        super(PaperT11EAPIMain.main, "clientcrash", "/clientcrash <player>", "api.clientcrash",
                Receiver.ALL);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (VersionHelper.aboveOr19()) {
                    target.spawnParticle(Particle.EXPLOSION_HUGE, target.getEyeLocation(), Integer.MAX_VALUE);
                    sender.sendMessage("Successfully crashed player!");
                } else
                    sender.sendMessage("Sorry, but your version doesn't support crashes!");
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
                if (!target.hasPermission("api.clientcrash.exempt")
                        || player.hasPermission("api.clientcrash.exempt.bypass")) {
                    if (VersionHelper.aboveOr19()) {
                        target.spawnParticle(Particle.EXPLOSION_HUGE, target.getEyeLocation(), Integer.MAX_VALUE);
                        player.sendMessage(PaperT11EAPIMain.PREFIX + "§aSuccessfully crashed player!");
                    } else
                        player.sendMessage(PaperT11EAPIMain.PREFIX + "Sorry, but your version doesn't support crashes!");
                } else
                    player.sendMessage(PaperT11EAPIMain.PREFIX + "You can't crash that player!");
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