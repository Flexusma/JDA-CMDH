package de.flexusma.jdacmdh.utils;

import de.flexusma.jdacmdh.CommandPreferences;
import de.flexusma.jdacmdh.database.Database;

import java.util.HashMap;

public class PreferenceManager {

    private static HashMap<String,Object> preferences = new HashMap<>();

    public static CommandPreferences getPref(String guildId){
        if(preferences.containsKey(guildId)){
            return (CommandPreferences) preferences.get(guildId);
        }

        CommandPreferences pref = Database.initPref(null,guildId);
        if(pref!=null){
            preferences.put(guildId,pref);
            return pref;
        }
        return null;
    }

    public static void savePref(String guildId, CommandPreferences pref){
        if(preferences.containsKey(guildId)){
            preferences.replace(guildId,pref);
        }
        Database.savePref(null,guildId,pref);
    }

}
