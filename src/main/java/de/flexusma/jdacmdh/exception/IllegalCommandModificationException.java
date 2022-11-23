package de.flexusma.jdacmdh.exception;

public class IllegalCommandModificationException extends Exception{
    String cmdId;
    public IllegalCommandModificationException(String commandId){
        this.cmdId=commandId;
    }

    @Override
    public String getMessage() {
        return "Tried to modify command ["+cmdId+"] after it has already been sent to discord, this change will not be passed to discord!";
    }
}
