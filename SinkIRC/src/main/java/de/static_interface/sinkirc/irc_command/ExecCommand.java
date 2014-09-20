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

import de.static_interface.sinklibrary.sender.IrcCommandSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.spongepowered.api.command.CommandSource;

public class ExecCommand extends IrcCommand {

    public ExecCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean isIrcOpOnly() {
        return true;
    }

    @Override
    public boolean useNotices() {
        return true;
    }

    @Override
    public boolean onExecute(final CommandSource cs, String label, String[] args) {
        final IrcCommandSource sender = (IrcCommandSource) cs;
        String commandWithArgs = "";
        int i = 0;
        for (String arg : args) {
            if (i == args.length) {
                break;
            }
            i++;
            if (commandWithArgs.isEmpty()) {
                commandWithArgs = arg;
                continue;
            }
            commandWithArgs = commandWithArgs + ' ' + arg;
        }

        final String finalCommandWithArgs = commandWithArgs;

        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(sender, finalCommandWithArgs);
            }
        });
        return true;
    }
}
