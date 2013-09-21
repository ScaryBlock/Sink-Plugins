package de.static_interface.sinkchat.channel.configuration;

import de.static_interface.sinkchat.SinkChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageHandler
{

    private static YamlConfiguration language = new YamlConfiguration();
    private static final File languageFilesPath = new File(SinkChat.getDataFolderStatic() + File.separator + ( "lang.yml" ));

    public static boolean init()
    {
        try
        {
            language.load(languageFilesPath);
        }
        catch (IOException e)
        {

            language.set("messages.playerJoins", "Du bist dem $CHANNEL$ Channel gejoint.");
            language.set("messages.playerLeaves", "Du hast den $CHANNEL$ Channel verlassen.");
            language.set("messages.noChannelGiven", "Du musst einen Channel angeben!");
            language.set("messages.channelUnknown", "$CHANNEL$ ist ein unbekannter Channel.");
            language.set("messages.list", "Folgende Channels sind bekannt: $CHANNELS$");
            language.set("messages.part", "Du bist in den folgenden Channels:");
            language.set("messages.help", "Folgende Befehle sind verfuegbar:");

            language.set("messages.permissions.general", "Du hast nicht genuegend Rechte um das zu tun.");
            language.set("messages.permissions.shout", "Du hast nicht genuegend Rechte um das zu tun.");
            language.set("messages.permissions.ask", "Du hast nicht genuegend Rechte um das zu tun.");
            language.set("messages.permissions.trade", "Du hast nicht genuegend Rechte um das zu tun.");

            try
            {
                language.save(languageFilesPath);
            }
            catch (IOException e1)
            {
                return false;
            }


        }
        catch (InvalidConfigurationException e)
        {
            languageFilesPath.delete();
            Bukkit.getLogger().severe("Invalid configuration detected ! Restoring default configuration ...");
            return init();
        }
        return true;
    }

    public static String getString(String key)
    {
        return ( language.getRoot().getString(key) );
    }

}