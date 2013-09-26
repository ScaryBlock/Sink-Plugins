package de.static_interface.sinkchat.channel;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Author: Trojaner
 * Date: 22.09.13
 */
public class ChannelUtil
{
    public static boolean sendMessage(Player player, String message, IChannel channel, String prefix, Character callByChar)
    {
        if (channel.contains(player))
        {
            return false;
        }
        if (message.toCharArray().length == 1 && message.toCharArray()[0] == callByChar)
        {
            return false;
        }
        String formattedMessage = message.substring(1);
        if (SinkLibrary.groupsAvailable())
        {
            User user = new User(player);
            formattedMessage = prefix + ChatColor.GRAY + "[" + user.getPrimaryGroup() + ChatColor.GRAY + "] " + player.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RESET + formattedMessage;
        }
        else
        {
            formattedMessage = prefix + player.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RESET + formattedMessage;
        }
        if (player.hasPermission("sinkchat.color"))
        {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }

        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (! ( channel.contains(target) ))
            {
                target.sendMessage(formattedMessage);
            }
        }
        Bukkit.getConsoleSender().sendMessage(formattedMessage);
        SinkLibrary.sendIRCMessage(formattedMessage);
        return true;
    }
}