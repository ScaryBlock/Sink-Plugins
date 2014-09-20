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

package de.static_interface.sinklibrary.command;

import de.static_interface.sinklibrary.exception.UnauthorizedAccessException;
import de.static_interface.sinklibrary.sender.IrcCommandSender;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Player;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Command implements CommandCallable {

    protected CommandSource sender;
    protected Game game;
    private String usage = null;

    public Command(Game game) {
        this.game= game;
    }

    @Override
    public final boolean call(CommandSource sender, String arguments, List<String> parents) {
        this.sender = sender;
        return onPreExecute(sender, arguments, arguments.split(" "), parents);
    }

    public boolean isPlayerOnly() {
        return false;
    }

    ;

    public boolean isIrcOnly() {
        return false;
    }

    public boolean isIrcOpOnly() {
        return false;
    }

    public boolean useNotices() {
        return false;
    }

    protected boolean onPreExecute(final CommandSource sender, final String label, final String[] args, List<String> parents) {
        if (isPlayerOnly() && isIrcOnly()) {
            throw new IllegalStateException("Commands can't be IRC only & Player only ath the same time");
        }

        if (isIrcOpOnly() && sender instanceof IrcCommandSender && !sender.isOp()) {
            throw new UnauthorizedAccessException();
        }

        if (!(sender instanceof IrcCommandSender) && isIrcOnly()) {
            return false;
        } else if (!(sender instanceof Player) && isPlayerOnly()) {
            return false;
        }
        boolean defaultNotices = false;
        if (useNotices() && sender instanceof IrcCommandSender) {
            defaultNotices = ((IrcCommandSender) sender).getUseNotice();
            ((IrcCommandSender) sender).setUseNotice(true);
        }

        //Todo: Sponge Scheduler is not implemented yet
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                Exception exception = null;
                boolean success = false;
                try {
                    success = onExecute(sender, label, args);
                } catch (Exception e) {
                    exception = e;
                }

                onPostExecute(sender, label, args, exception, success);
            }
        });

        if (useNotices() && sender instanceof IrcCommandSender) {
            ((IrcCommandSender) sender).setUseNotice(defaultNotices);
        }

        return true;
    }

    protected abstract boolean onExecute(CommandSource sender, String label, String[] args);

    protected void onPostExecute(CommandSource sender, String label, String[] args, Exception exception, boolean success) {
        if (exception instanceof UnauthorizedAccessException) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have access to this command.");
            return;
        }

        if (exception != null) {
            exception.printStackTrace();
        }

        if (exception != null) {
            sender.sendMessage(exception.getMessage());
        }

        if (!success && getUsage() != null) {
            sender.sendMessage(getUsage());
        }
    }

    protected String getCommandPrefix() {
        if (sender instanceof IrcCommandSender) {
            return getIrcCommandPrefix();
        }
        return "/";
    }

    protected String getIrcCommandPrefix() {
        try {
            Class<?> c = Class.forName("de.static_interface.sinkirc.IrcUtil");
            Method method = c.getMethod("getCommandPrefix", null);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return (String) method.invoke(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
