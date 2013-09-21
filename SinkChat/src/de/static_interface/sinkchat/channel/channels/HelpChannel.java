package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class HelpChannel extends JavaPlugin implements IChannel
{

    Vector<Player> exceptedPlayers = new Vector<>();
    String PREFIX = ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "Frage" + ChatColor.GRAY + "]" + ChatColor.RESET;
    private char callByChar = '?';

    public HelpChannel(char callChar)
    {
        callByChar = callChar;
    }

    @Override
    public void addExceptedPlayer(Player player)
    {
        exceptedPlayers.add(player);
    }

    @Override
    public void removeExceptedPlayer(Player player)
    {
        exceptedPlayers.remove(player);
    }

    @Override
    public boolean contains(Player player)
    {
        return exceptedPlayers.contains(player);
    }

    @Override
    public String getChannelName()
    {
        return "Frage";
    }

    @Override
    public void sendMessage(Player player, String message)
    {
        String formattedMessage = message.substring(1);
        formattedMessage = this.PREFIX + " [" + SinkChat.getGroup(player) + ChatColor.RESET + "] " + SinkChat.getDisplayName(player) + ": " + formattedMessage;
        if (player.hasPermission("sinkchat.color"))
        {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }
        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (! ( this.contains(target) ))
            {
                target.sendMessage(formattedMessage);
            }
        }
        SinkLibrary.sendIRCMessage(formattedMessage);
    }

    @Override
    public void registerChannel()
    {
        ChannelHandler.registerChannel(this, "Frage", callByChar);
    }
}