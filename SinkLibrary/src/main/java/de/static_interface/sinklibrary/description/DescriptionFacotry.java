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

import java.util.List;

import javax.annotation.Nullable;

public class DescriptionFacotry {
    String shortDescription = null;
    String help = null;
    String usage;
    List<String> permissions;

    public DescriptionFacotry(String usage, List<String> permissions) {
        this.usage = usage;
        this.permissions = permissions;
    }

    public DescriptionFacotry setShortDescription(@Nullable String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public DescriptionFacotry setHelp(@Nullable String help) {
        this.help = help;
        return this;
    }

    public SinkDescription build(){
        SinkDescription description = new SinkDescription();
        description.setShortDescription(shortDescription);
        description.setHelp(help);
        description.setUsage(usage);
        description.setPermissions(permissions);

        return description;
    }
}
