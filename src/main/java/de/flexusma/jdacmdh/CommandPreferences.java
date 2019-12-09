package de.flexusma.jdacmdh;


import javax.annotation.Nullable;
import java.io.Serializable;

public class CommandPreferences implements Serializable {

    private String Prefix = "!";
    private Emoticons emoticons = new Emoticons("✔","⚠","✘");



    public CommandPreferences(@Nullable String prefix, @Nullable Emoticons emoticons) {
        if(prefix!=null) this.setPrefix(prefix);
        if(emoticons!=null) this.setEmoticons(emoticons);
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
}
