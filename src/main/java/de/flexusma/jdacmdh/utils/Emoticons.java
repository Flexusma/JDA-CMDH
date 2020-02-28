/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * CC0 1.0 Universal
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */

package de.flexusma.jdacmdh.utils;

import java.io.Serializable;

public class Emoticons implements Serializable {

    public String success;
    public String warn;
    public String error;


        public Emoticons(String success, String warn, String f){
            this.success=success;
            this.warn=warn;
            this.error=f;
        }

}
