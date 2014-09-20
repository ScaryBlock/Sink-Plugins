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

import de.static_interface.sinklibrary.util.BukkitUtil;
import de.static_interface.sinklibrary.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeamchatCommand implements CommandExecutor {

    public static final String
            PREFIX =
            ChatColor.GRAY + "[" + ChatColor.DARK_RED + 'T' + ChatColor.RED + "eam" + ChatColor.DARK_RED + 'C' + ChatColor.RED + "hat"
            + ChatColor.GRAY + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String message = Util.formatArrayToString(args, " ");
        BukkitUtil.broadcast(PREFIX + BukkitUtil.getSenderName(sender) + ChatColor.WHITE + ": " + message, "sinkcommands.teamchat", false);
        return true;
    }
}
