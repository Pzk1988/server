package com.homeAutomation.Server.Exceptions;

public class PostfixParseException extends Exception{

    public PostfixParseException(String exp){
        this.exp = exp;
    }
    public String getExp(){
        return exp;
    }

    private String exp;
}
