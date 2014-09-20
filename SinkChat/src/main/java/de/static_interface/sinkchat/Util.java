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

package de.static_interface.sinkchat;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration.m;

import de.static_interface.sinklibrary.util.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.SinkUser;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Util {

    public static void sendMessage(SinkUser user, String message, int range) {

        double x = user.getPlayer().getLocation().getX();
        double y = user.getPlayer().getLocation().getY();
        double z = user.getPlayer().getLocation().getZ();

        for (Player p : BukkitUtil.getOnlinePlayers()) {
            Location loc = p.getLocation();
            boolean isInRange = Math.abs(x - loc.getX()) <= range && Math.abs(y - loc.getY()) <= range && Math.abs(z - loc.getZ()) <= range;

            SinkUser onlineUser = SinkLibrary.getInstance().getUser(p);

            // Check for spy
            boolean canSpy = onlineUser.hasPermission("sinkchat.spy.all") || (onlineUser.hasPermission("sinkchat.spy")
                                                                              && !user.hasPermission("sinkchat.spy.bypass"));

            PlayerConfiguration config = onlineUser.getPlayerConfiguration();

            if (isInRange) {
                p.sendMessage(message);
            } else if (canSpy && config.isSpyEnabled()) {
                p.sendMessage(getSpyPrefix() + message);
            }
        }
    }

    public static String getSpyPrefix() {
        return ChatColor.GRAY + m("SinkChat.Prefix.Spy") + ' ' + ChatColor.RESET;
    }
}
