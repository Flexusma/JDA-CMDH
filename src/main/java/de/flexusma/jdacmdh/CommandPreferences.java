/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * CC0 1.0 Universal
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh;


import de.flexusma.jdacmdh.utils.Emoticons;

import javax.annotation.Nullable;
import java.io.Serializable;

public class CommandPreferences implements Serializable {

    private String Prefix = "!";
    private int volume = 100;
    private Emoticons emoticons = new Emoticons("✔","⚠","✘");



    public CommandPreferences(@Nullable String prefix, @Nullable Emoticons emoticons) {
        if(prefix!=null) this.setPrefix(prefix);
        if(emoticons!=null) this.setEmoticons(emoticons);
    }


    public CommandPreferences(@Nullable String prefix) {
        if(prefix!=null) this.setPrefix(prefix);
    }


    public CommandPreferences() {

    }


    //Setter and Getter

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public Emoticons getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(Emoticons emoticons) {
        this.emoticons = emoticons;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}

