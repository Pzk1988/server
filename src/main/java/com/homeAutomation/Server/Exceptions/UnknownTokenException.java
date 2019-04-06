package com.homeAutomation.Server.Exceptions;

public class UnknownTokenException extends Exception{
    public UnknownTokenException( String token ){
        System.out.println("Unknown token: " + token);
    }
}
