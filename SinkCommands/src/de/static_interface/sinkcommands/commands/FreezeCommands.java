package de.static_interface.sinkcommands.commands;

import de.static_interface.sinkcommands.SinkCommands;
import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreezeCommands
{
    public static final String PREFIX = ChatColor.RED + "[Freeze] " + ChatColor.RESET;

    public static Set<String> toFreeze = new HashSet<>();
    private static ArrayList<String[]> toTmpFreeze = new ArrayList<>();
    public static Set<String> freezeAll = new HashSet<>();

    public static class FreezeCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length < 1)
            {
                return false;
            }

            String reason = "";

            for (int i = 1; i < args.length; i++)
            {
                if (reason.equals(""))
                {
                    reason = args[i];
                    continue;
                }
                reason = reason + " " + args[i];
            }

            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase()) || p.getName().equalsIgnoreCase(args[0]))
                {
                    if (! canBeFrozen(p) && sender instanceof Player)
                    {
                        sender.sendMessage(PREFIX + ChatColor.DARK_RED + "Dieser Spieler kann nicht eingefroren werden!");
                        return true;
                    }
                    if (toggleFreeze(p))
                    {
                        if (args.length < 2)
                        {
                            BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " eingefroren.", "sinkcommands.freeze.message");
                        }
                        else
                        {
                            BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " eingefroren. Grund: " + reason, "sinkcommands.freeze.message");
                        }
                        return true;
                    }
                    p.sendMessage(PREFIX + ChatColor.RED + "Du wurdest von " + BukkitUtil.getSenderName(sender) + " aufgetaut.");
                    BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " wieder aufgetaut.", "sinkcommands.freeze.message");
                    return true;
                }
            }
            return true;
        }
    }

    public static class TmpfreezeCommand implements CommandExecutor
    {


        private final SinkCommands plugin;

        public TmpfreezeCommand(SinkCommands plugin)
        {
            this.plugin = plugin;
        }


        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            sender.sendMessage(ChatColor.RED + "Dieser Command wurde deaktiviert");
            return true;
            /*
            if (args.length < 3)
            {
                return false;
            }
            String reason;

            List<String> tmp = new ArrayList<>();

            int i = - 1;
            for (String s : args)
            {
                i++;
                if (i <= 1)
                {
                    continue;
                }
                tmp.add(s);
            }

            reason = Util.formatArrayToString((String[]) tmp.toArray(), " ");

            int seconds = 0;
            if (! Util.isNumber(args[1]) || Integer.parseInt(args[1]) <= 0)
            {
                Pattern p = Pattern.compile("(\\d)+d");
                Matcher m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += 86400 * Integer.parseInt(m.group(1).trim());
                }

                p = Pattern.compile("(\\d)+h");
                m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += 3600 * Integer.parseInt(m.group(1).trim());
                }

                p = Pattern.compile("(\\d)+m");
                m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += 60 * Integer.parseInt(m.group(1).trim());
                }

                p = Pattern.compile("(\\d)+s");
                m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += Integer.parseInt(m.group(1).trim());
                }

                if (seconds <= 0)
                {
                    sender.sendMessage(PREFIX + ChatColor.RED + "Ungültige Zeit (muss größer als 0 sein)!");
                    return true;
                }
            }
            else
            {
                seconds = Integer.parseInt(args[1]);
            }

            for (final Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase()) || p.getName().equalsIgnoreCase(args[0]))
                {
                    if (! temporarilyFreeze(p, seconds, plugin))
                    {
                        sender.sendMessage(PREFIX + "Dieser Spieler wurde schon eingefroren");
                        return true;
                    }
                    p.sendMessage(PREFIX + ChatColor.RED + "Du wurdest von " + BukkitUtil.getSenderName(sender) + " für " + seconds + " Sekunden eingefroren. Grund: " + reason);
                    BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " für " + seconds + " Sekunden eingefroren. Grund: " + reason, "sinkcommands.freeze.message");
                }
            }
            return true;
            */
        }
    }

    public static class FreezeallCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length < 1 && freezeAll.size() != Bukkit.getWorlds().size())
            {
                return false;
            }

            String reason = Util.formatArrayToString(args, " ");

            if (toggleFreezeAll())
            {
                BukkitUtil.broadcastMessage(PREFIX + ChatColor.RED + "Alle Spieler wurden von " + BukkitUtil.getSenderName(sender) + " eingefroren. Grund: " + reason);
            }
            else
            {
                BukkitUtil.broadcastMessage(PREFIX + ChatColor.RED + "Alle Spieler wurden von " + BukkitUtil.getSenderName(sender) + " aufgetaut.");
            }
            return true;
        }
    }

    public static class FreezelistCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            String frozenList = "";
            String tmpfrozenList = "";
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (isFrozen(p))
                {
                    if (frozenList.length() > 0)
                    {
                        frozenList = frozenList + ", " + p.getDisplayName();
                    }
                    else
                    {
                        frozenList = p.getDisplayName();
                    }
                }

                if (isTmpFrozen(p) > 0)
                {
                    if (tmpfrozenList.length() > 0)
                    {
                        tmpfrozenList = tmpfrozenList + ", " + p.getDisplayName() + " für " + isTmpFrozen(p) + " Sekunden";
                    }
                    else
                    {
                        tmpfrozenList = p.getDisplayName() + " für " + isTmpFrozen(p) + " Sekunden";
                    }
                }
            }


            if (frozenList.length() > 0)
            {
                sender.sendMessage(PREFIX + "Eingefrorene Spieler: " + frozenList);
            }
            else
            {
                sender.sendMessage(PREFIX + "Es gibt keine eingefrorenen Spieler.");
            }

            if (tmpfrozenList.length() > 0)
            {
                sender.sendMessage(PREFIX + "Temporär eingefrorene Spieler: " + tmpfrozenList);
            }
            else
            {
                sender.sendMessage(PREFIX + "Es gibt keine temporär eingefrorenen Spieler.");
            }
            return true;
        }
    }

    public static boolean isFrozen(Player player)
    {
        if (player == null)
        {
            return false;
        }

        if (freezeAll.contains(player.getWorld().getName()))
        {
            return canBeFrozen(player);
        }

        return toFreeze.contains(player.getName());
    }

    public static boolean canBeFrozen(Player player)
    {
        return ! player.hasPermission("sinkcommands.freeze.never");
    }

    public static int isTmpFrozen(Player player)
    {
        if (player == null)
        {
            return - 1;
        }

        for (String[] string : toTmpFreeze)
        {
            if (player.getName().equalsIgnoreCase(string[0]))
            {
                return Integer.parseInt(string[1]);
            }
        }
        return 0;
    }

    public static boolean toggleFreezeAll()
    {
        if (freezeAll.size() == Bukkit.getWorlds().size())
        {
            freezeAll.clear();
            return false;
        }

        for (World w : Bukkit.getWorlds())
        {
            if (! freezeAll.contains(w.getName()))
            {
                freezeAll.add(w.getName());
            }
        }
        return true;
    }

    public static boolean toggleFreeze(Player player)
    {
        if (toTmpFreeze.contains(player.getName()))
        {
            toTmpFreeze.remove(player.getName());
        }
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
            return false;
        }
        else
        {
            toFreeze.add(player.getName());
            return true;
        }
    }

    public static void unfreeze(Player player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
        }
        if (toTmpFreeze.contains(player.getName()))
        {
            toTmpFreeze.remove(player.getName());
        }
    }

    public static void unfreeze(OfflinePlayer player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
        }
        if (toTmpFreeze.contains(player.getName()))
        {
            toTmpFreeze.remove(player.getName());
        }
    }

    /*
    public static boolean temporarilyFreeze(final Player player, final int seconds, Plugin plugin)
    {
        if (! toFreeze.contains(player.getName()) && ! toTmpFreeze.contains(player.getName()))
        {
            toFreeze.add(player.getName());
            String[] array = { player.getName(), String.valueOf(seconds) };
            final int IDListArray = toTmpFreeze.size();
            toTmpFreeze.add(IDListArray, array);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if (! toTmpFreeze.contains(player.getName()))
                    {
                        return;
                    }
                    unfreeze(player);
                    toTmpFreeze.remove(IDListArray);
                    player.sendMessage(ChatColor.RED + "[Freeze]" + ChatColor.WHITE + " Du wurdest automatisch wieder aufgetaut.");
                }
            }, seconds * 20);
            return true;
        }
        else
        {
            return false;
        }
    }
    */
    public static void loadFreezedPlayers(Logger log, File dataFolder, Plugin plugin)
    {
        File saveFile = new File(dataFolder, "freezedPlayers.txt");
        if (saveFile.exists())
        {
            try
            {
                InputStream ips = new FileInputStream(saveFile);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String line;
                while (( line = br.readLine() ) != null)
                {
                    toFreeze.add(line.trim());
                }
            }
            catch (Exception exc)
            {
                log.log(Level.SEVERE, "Fehler beim laden der freezedPlayers.txt ", exc);
            }
        }
        saveFile = new File(dataFolder, "freezedTmpPlayers.txt");
        if (saveFile.exists())
        {
            try
            {
                InputStream ips = new FileInputStream(saveFile);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String line;
                while (( line = br.readLine() ) != null)
                {
                    Pattern p = Pattern.compile("(.*):(.*);");
                    Matcher m = p.matcher(line);
                    while (m.find())
                    {
                        String array[] = { m.group(1).trim(), m.group(2).trim() };
                        final String playerName = m.group(1).trim();
                        final long time = Long.parseLong(m.group(2).trim());
                        final int IDListArray = toTmpFreeze.size();
                        if (Bukkit.getOfflinePlayer(playerName) != null)
                        {
                            toTmpFreeze.add(IDListArray, array);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
                                    if (player != null)
                                    {
                                        unfreeze(player);
                                        toTmpFreeze.remove(IDListArray);
                                        Bukkit.getPlayer(player.getName()).sendMessage("Du wurdest nach " + time + " Sekunden ungefreezt.");
                                    }
                                }
                            }, time * 20);
                            if (! toFreeze.contains(playerName))
                            {
                                toFreeze.add(playerName);
                            }
                        }
                    }
                }
            }
            catch (Exception exc)
            {
                log.log(Level.SEVERE, "Fehler beim laden der freezedTmpPlayers.txt ", exc);
            }
        }
    }

    public static void unloadFreezedPlayers(Logger log, File dataFolder)
    {
        if (! dataFolder.exists())
        {
            dataFolder.mkdirs();
        }
        File saveFile = new File(dataFolder, "freezedPlayers.txt");
        if (saveFile.exists())
        {
            saveFile.delete();
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));

            for (String freezedPlayer : toFreeze)
            {
                if (! toTmpFreeze.contains(freezedPlayer))
                {
                    out.write(freezedPlayer + "\n");
                }
            }

            out.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Fehler beim speichern der freezedPlayers.txt ", e);
        }

        saveFile = new File(dataFolder, "freezedTmpPlayers.txt");
        if (saveFile.exists())
        {
            saveFile.delete();
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));

            for (String[] array : toTmpFreeze)
            {
                out.write(array[0] + ":" + array[1] + ";\n");
            }
            out.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Fehler beim speichern der freezedPlayers.txt ", e);
        }
    }
}