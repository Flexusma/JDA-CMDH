package de.flexusma.jdacmdh.exception;

public class IllegalObjectModificationException extends Exception{
    String cmdId;
    public IllegalObjectModificationException(String commandId){
        this.cmdId=commandId;
    }

    @Override
    public String getMessage() {
        return "Tried to modify command ["+cmdId+"] after it has already been sent to discord, this change will not be passed to discord!";
    }
}
