/*
 * Copyright (c) 2014 http://adventuria.eu, http://static-interface.de and contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.static_interface.sinkcommands.command;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.SinkUser;
import de.static_interface.sinklibrary.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements CommandExecutor {

    public static final String PREFIX = ChatColor.AQUA + "[Rename] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SinkUser user = SinkLibrary.getInstance().getUser(sender);
        if (user.isConsole()) {
            sender.sendMessage(PREFIX + "Dieser Befehl ist nur Ingame ausführbar.");
            return true;
        }
        Player p = user.getPlayer();

        if (args.length < 1) {
            return false;
        }
        if (p.getItemInHand().getType() == Material.AIR) {
            sender.sendMessage(PREFIX + "Nimm ein Item in die Hand bevor du diesen Befehl ausführst.");
            return true;
        }
        String text = Util.formatArrayToString(args, " ");

        text = ChatColor.translateAlternateColorCodes('&', text);
        ItemStack item = p.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text);
        sender.sendMessage(PREFIX + ChatColor.GRAY + "Name von Item wurde zu: " + ChatColor.GREEN + text + ChatColor.GRAY + " umbenannt.");
        item.setItemMeta(meta);
        return true;
    }
}