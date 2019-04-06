package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"out","expression"})
public class InfixExpression {

    public InfixExpression(){}
    public InfixExpression(String out, String expression){
        this.out = out;
        this.expression = expression; }

    @XmlAttribute
    public String getOut() {
        return out;
    }
    public void setOut(String out) {
        this.out = out;
    }

    @XmlAttribute
    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }

    private String expression;
    private String out;

    @Override
    public String toString(){
        return "Infix: " + out + " = " + expression + "\r\n";
    }
}
