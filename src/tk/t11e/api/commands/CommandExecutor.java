package tk.t11e.api.commands;
// Created by booky10 in BungeeT11E (19:07 03.02.20)

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tk.t11e.api.main.Main;

import java.util.*;

@SuppressWarnings("unused")
public abstract class CommandExecutor {

    private final String command, permission,usage;
    private final String[] aliases;
    private final Receiver receiver;
    private final JavaPlugin plugin;
    private final Executor executor;

    public CommandExecutor(JavaPlugin plugin, String command, String usage, Receiver receiver) {
        this(plugin, command,usage, "",receiver);
    }

    public CommandExecutor(JavaPlugin plugin, String command, String usage, String permission,
                           Receiver receiver, String... aliases) {
        this.command = command;
        this.permission = permission;
        this.usage=usage;
        this.aliases = aliases;
        this.receiver=receiver;
        this.plugin = plugin;

        this.executor=new Executor();
    }

    public Boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    public String getCommand() {
        return command;
    }

    public Executor getExecutor() {
        return executor;
    }

    public String getPermission() {
        return permission;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public Boolean arePlayersAllowed() {
        return receiver.equals(Receiver.ALL)||receiver.equals(Receiver.PLAYER);
    }

    public Boolean areConsolesAllowed() {
        return receiver.equals(Receiver.ALL)||receiver.equals(Receiver.CONSOLE);
    }

    public CommandExecutor init() {
        Objects.requireNonNull(plugin.getCommand(getCommand())).setExecutor(getExecutor());
        Objects.requireNonNull(plugin.getCommand(getCommand())).setAliases(getAliases());
        return this;
    }

    public List<String> getAliases() {
        return Arrays.asList(aliases);
    }

    public String getUsage() {
        return usage;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void help(CommandSender sender) {
        if(sender instanceof Player)
            sender.sendMessage(Main.PREFIX+"Usage: "+getUsage());
        else
            sender.sendMessage("Usage: "+getUsage());
    }

    public List<String> getOnlinePlayerNames() {
        List<String> names=new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            names.add(player.getDisplayName());
        return names;
    }

    public List<String> convertTab(String[] args, List<String> completions) {
        if(completions==null)
            return Collections.emptyList();
        List<String> list = new ArrayList<>();

        if (args.length < 1)
            return Collections.emptyList();
        String word = args[args.length - 1];
        if (word.equalsIgnoreCase(""))
            return completions;

        for (String entry : completions)
            if (entry.startsWith(word)&& !entry.equals(word))
                list.add(entry);

        return list;
    }

    public abstract void onExecute(CommandSender sender, String[] args, Integer length);

    public abstract void onPlayerExecute(Player player, String[] args, Integer length);

    public abstract List<String> onComplete(CommandSender sender, String[] args, Integer length);

    protected enum Receiver {
        ALL,
        PLAYER,
        CONSOLE
    }

    @SuppressWarnings("NullableProblems")
    class Executor implements TabExecutor, org.bukkit.command.CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
            if (sender instanceof Player && arePlayersAllowed()) {
                Player player = (Player) sender;
                if (getPermission().equals(""))
                    onPlayerExecute(player, args, args.length);
                else if (hasPermission(player))
                    onPlayerExecute(player, args, args.length);
                else
                    player.sendMessage(Main.NO_PERMISSION);
            } else if (areConsolesAllowed())
                onExecute(sender, args, args.length);
            else
                sender.sendMessage("You must execute this command as a player!");
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label,
                                              String[] args) {
                return convertTab(args, onComplete(sender, args, args.length));
        }
    }
}