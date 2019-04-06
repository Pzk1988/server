package com.homeAutomation.Server.Model;

public class Operator extends Token {
    public Operator(OperatoryType type){
        super(TokenType.OPERATOR);
        this.operatoryType = type;
        if(type == OperatoryType.AND){
            name = "&";
        } else if(type == OperatoryType.OR){
            name ="|";
        } else if(type == OperatoryType.GRT){
            name =">";
        } else if(type == OperatoryType.GRT_EQ){
            name =">=";
        } else if(type == OperatoryType.LESS){
            name ="<";
        } else if(type == OperatoryType.LESS_EQ){
            name ="<=";
        }
    }
    public int getValue() {
        return operatoryType.getValue();
    }

    public String getName() {
        return name;
    }

    private OperatoryType operatoryType;
    private String name;
}
