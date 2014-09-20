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

import static de.static_interface.sinklibrary.Constants.COMMAND_PREFIX;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.SinkUser;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import de.static_interface.sinklibrary.description.DescriptionFacotry;
import de.static_interface.sinklibrary.util.Util;
import org.bukkit.ChatColor;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.Description;

import java.util.ArrayList;
import java.util.List;

public class SinkDebugCommand extends Command {

    public static final String PREFIX = ChatColor.BLUE + "[Debug] " + ChatColor.RESET;

    String cmd = getCommandPrefix() + "sdebug";

    public SinkDebugCommand(Game game) {
        super(game);
    }

    @Override
    public boolean isIrcOpOnly() {
        return true;
    }

    public boolean onExecute(CommandSource sender, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String option = args[0];

        switch (option.toLowerCase()) {
            case "getplayervalue": {
                if (args.length != 3) {
                    sender.sendMessage(PREFIX + "Wrong Usage! Correct Usage: " + COMMAND_PREFIX + cmd + " getplayervalue <player> <path.to.key>");
                    break;
                }
                String player = args[1];
                String path = args[2];
                SinkUser user = SinkLibrary.getInstance().getUser(player);
                PlayerConfiguration config = user.getPlayerConfiguration();
                sender.sendMessage(PREFIX + "Output: " + config.get(path));
                break;
            }

            case "setplayervalue": {
                if (args.length != 4) {
                    sender.sendMessage(
                            PREFIX + "Wrong Usage! Correct Usage: " + COMMAND_PREFIX + cmd + " setplayervalue <player> <path.to.key> <value>");
                    break;
                }
                String player = args[1];
                String path = args[2];
                Object value = replaceValue(args[3]);

                SinkUser user = SinkLibrary.getInstance().getUser(player);
                PlayerConfiguration config = user.getPlayerConfiguration();
                config.set(path, value);
                sender.sendMessage(PREFIX + "Done");
                break;
            }

            case "haspermission": {
                if (args.length != 3) {
                    sender.sendMessage(PREFIX + "Wrong Usage! Correct Usage: " + COMMAND_PREFIX + cmd + " haspermission <player> <permission>");
                    break;
                }
                String player = args[1];
                String permission = args[2];
                SinkUser user = SinkLibrary.getInstance().getUser(player);
                sender.sendMessage(PREFIX + "Output: " + user.hasPermission(permission));
                break;

            }

            case "backuplanguage": {
                try {
                    LanguageConfiguration.getInstance().backup();
                    sender.sendMessage(PREFIX + ChatColor.GREEN + "Done");
                } catch (Exception e) {
                    sender.sendMessage(PREFIX + ChatColor.RED + "Failed: ");
                    sender.sendMessage(PREFIX + ChatColor.RED + e.getMessage());
                }
                break;
            }

            case "isop": {
                boolean isOp = sender.isOp();
                sender.sendMessage(option + ": " + isOp);
                break;
            }

            case "testop": {
                boolean isOp = sender.isOp();
                if (isOp) {
                    sender.sendMessage(option + ": setting value: " + false);
                    sender.setOp(false);
                    sender.sendMessage(option + ": restoring value: " + true);
                    sender.setOp(true);
                    break;
                }
            }

            default: {
                sender.sendMessage(PREFIX + "Available options: getplayervalue, setplayervalue, backuplanguage, isop, testop");
            }
        }
        return true;
    }

    private Object replaceValue(String value) {
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        if (Util.isNumber(value)) {
            return Integer.parseInt(value);
        }
        return value;
    }

    @Override
    public Description getDescription() {
        List<String> perms = new ArrayList<>();
        perms.add("sinklibrary.debug");
        DescriptionFacotry factory = new DescriptionFacotry("/sdebug <option> <args>", perms);
        return factory.setShortDescription("Allows debugging").setHelp(null).build();
    }

    @Override
    public boolean testPermission(CommandSource commandSource) {
        return false;
    }

    @Override
    public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
        return null;
    }
}
