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

package de.static_interface.sinklibrary.description;

import org.spongepowered.api.command.Description;

import java.util.List;

import javax.annotation.Nullable;

public class SinkDescription implements Description {
    String shortDescription;
    String help;
    String usage;
    List<String> permissions;

    @Nullable
    @Override
    public String getShortDescription() {
        return null;
    }

    @Nullable
    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getPermissions() {
        return null;
    }

    public void setShortDescription(@Nullable String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setHelp(@Nullable String help) {
        this.help = help;
    }

    public void setUsage(String usage){
        this.usage = usage;
    }

    public void setPermissions(List<String> permissions){
        this.permissions = permissions;
    }
}
