/*
 * Copyright (c) 2013 - 2014 http://adventuria.eu, http://static-interface.de and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.static_interface.sinkirc.irc_command;

import de.static_interface.sinkirc.IrcUtil;
import de.static_interface.sinklibrary.util.BukkitUtil;
import de.static_interface.sinklibrary.sender.IrcCommandSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class SayCommand extends IrcCommand {

    public SayCommand(Plugin plugin) {
        super(plugin);
        setUsage("Usage: " + IrcUtil.getCommandPrefix() + "say <text>");
    }

    @Override
    public boolean onExecute(CommandSender cs, String label, String[] args) {
        IrcCommandSender sender = (IrcCommandSender) cs;
        if (args.length < 1) {
            return false;
        }

        String source;

        if (isQueryCommand()) {
            source = "Query";
        } else {
            source = sender.getSource();
        }

        String messageWithPrefix;
        messageWithPrefix =
                IRC_PREFIX + ChatColor.GRAY + '[' + source + "] " + IrcUtil.getFormattedName(sender.getUser()) + ChatColor.GRAY + ": "
                + ChatColor.WHITE + label.replaceFirst("say", "");

        BukkitUtil.broadcastMessage(messageWithPrefix);
        return true;
    }
}
