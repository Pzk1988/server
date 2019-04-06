package com.homeAutomation.Server.Model;

public class Constant extends Token{
    public Constant(Float value){
        super(TokenType.OPERAND);
        this.value = Math.round(value * 100);
    }
    public int getValue() {
        return value;
    }

    public String getName() {
        return "Constant";
    }

    private int value;
}
