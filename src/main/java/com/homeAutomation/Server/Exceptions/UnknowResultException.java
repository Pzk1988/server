package com.homeAutomation.Server.Exceptions;

public class UnknowResultException extends Exception {
    public UnknowResultException(String out){
        this.out = out;
    }

    public String getOutput(){
        return out;
    }

    private String out;
}
