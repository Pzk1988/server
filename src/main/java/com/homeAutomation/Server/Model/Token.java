package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.XmlTransient;

public abstract class Token{
    public enum TokenType{
        OPERATOR, OPERAND
    }
    public enum TokenActivity{
        RISING_EDGE,
        FALLING_EDGE,
        LOW,
        HIGH
    }

    public Token(){
        activity = TokenActivity.HIGH;
    }
    public Token(TokenType type){
        this.type = type;
        this.activity = TokenActivity.HIGH;
        this.outOfControl = true;
    }

    @XmlTransient
    public void setActivity(TokenActivity activity){
        this.activity = activity;
    }
    public  TokenActivity getActivity(){
        return  activity;
    }

    @XmlTransient
    public TokenType getType(){ return type; }

    @XmlTransient
    public abstract int getValue();
    public abstract String getName();

    @XmlTransient
    public int getPrevValue(){
        return getValue();
    }

    public boolean getOutOfControl(){ return false; }

    private TokenType type;
    private TokenActivity activity;
    protected boolean outOfControl;
}
