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
import de.static_interface.sinklibrary.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.command.CommandSource;

import java.text.DecimalFormat;

public class LagCommand extends Command {

    public static final String PREFIX = ChatColor.DARK_PURPLE + "[Lag] " + ChatColor.RESET;

    public LagCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onExecute(CommandSource sender, String label, String[] args) {
        double realTPS = SinkLibrary.getInstance().getSinkTimer().getAverageTPS();
        DecimalFormat decimalFormat = new DecimalFormat("##.0");
        String shownTPS = decimalFormat.format(realTPS);
        if (realTPS >= 18.5) {
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Der Server läuft ohne Probleme!");
        } else if (realTPS >= 17) {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        } else {
            sender.sendMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        sender.sendMessage(PREFIX + "(TPS: " + shownTPS + ')');
        return true;
    }
}

