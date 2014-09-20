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

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.command.Command;
import de.static_interface.sinklibrary.sender.IrcCommandSource;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.command.CommandSource;

public class HelpCommand extends IrcCommand {

    public HelpCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onExecute(CommandSource cs, String label, String[] args) {
        IrcCommandSource sender = (IrcCommandSource) cs;
        String cmds = "";
        for (String cmdName : SinkLibrary.getInstance().getCommands().keySet()) {
            Command cmd = SinkLibrary.getInstance().getCommands().get(cmdName);
            if (!cmd.isPlayerOnly()) {
                if (cmds.equals("")) {
                    cmds = cmdName;
                    continue;
                }
                cmds += ", " + cmdName;
            }
        }

        sender.sendMessage("Available commands: " + cmds);
        return true;
    }
}
