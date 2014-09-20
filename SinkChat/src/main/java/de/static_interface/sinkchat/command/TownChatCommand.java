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

package de.static_interface.sinkchat.command;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration.m;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import de.static_interface.sinkchat.TownyBridge;
import de.static_interface.sinklibrary.util.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

public class TownChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(m("General.ConsoleNotAvailable"));
            return true;
        }

        Player player = (Player) sender;
        Resident resident = TownyBridge.getResident(player.getName());

        if (!resident.hasTown()) {
            player.sendMessage(m("SinkChat.Towny.NotInTown"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(m("SinkChat.Towny.NoArguments"));
            return true;
        }

        Town town;
        try {
            town = resident.getTown();
        } catch (NotRegisteredException ignored) //Shouldn't happen...
        {
            return true;
        }

        String msg = "";
        for (String arg : args) {
            msg += arg + ' ';
        }

        msg = msg.trim();

        String prefixName = TownyBridge.getFormattedResidentName(resident, true, false);

        String
                formattedMessage =
                ChatColor.GRAY + "[" + ChatColor.GOLD + town.getName() + ChatColor.GRAY + "] " + prefixName + ChatColor.GRAY + ": " + ChatColor.WHITE
                + msg;

        ArrayList<Player> sendPlayers = new ArrayList<>();

        for (Resident townResident : town.getResidents()) {
            if (townResident.isNPC()) {
                continue;
            }
            Player onlineResident = BukkitUtil.getPlayer(townResident.getName());
            if (onlineResident == null) {
                continue;
            }
            sendPlayers.add(onlineResident);
        }

        for (Player onlinePlayer : BukkitUtil.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("sinkchat.townyspy")) {
                continue;
            }
            if (sendPlayers.contains(onlinePlayer)) {
                continue;
            }
            sendPlayers.add(onlinePlayer);
        }

        for (Player p : sendPlayers) {
            p.sendMessage(formattedMessage);
        }

        SinkLibrary.getInstance().getCustomLogger().log(Level.INFO, formattedMessage);
        return true;
    }
}
