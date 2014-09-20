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

package de.static_interface.sinklibrary.command;

import de.static_interface.sinklibrary.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.command.CommandSource;

import java.util.List;

public class SinkVersionrCommand extends Command {

    public static final String PREFIX = ChatColor.BLUE + "[SinkCommands] " + ChatColor.RESET;

    Plugin plugin;

    public SinkVersionrCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onExecute(CommandSource sender, String label, String[] args) {
        List<String> authorsList = plugin.getDescription().getAuthors();
        String authors = Util.formatPlayerListToString(authorsList);
        sender.sendMessage(PREFIX + plugin.getDescription().getName() + " by " + authors);
        sender.sendMessage(PREFIX + "Version: " + plugin.getDescription().getVersion());
        sender.sendMessage(PREFIX + "Copyright © 2013 - 2014 Adventuria / static-interface.de");
        return true;
    }
}
