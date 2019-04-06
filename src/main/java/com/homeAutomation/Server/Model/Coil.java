package com.homeAutomation.Server.Model;

import com.homeAutomation.Server.Interface.ILogicExpResult;
import com.homeAutomation.Server.Logger.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class Coil extends Token implements ILogicExpResult {
    public Coil(){
        super(TokenType.OPERAND);
    }
    public Coil(String name){
        super(TokenType.OPERAND);
        this.name = name;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    @XmlTransient
    public boolean getOutOfControl() {
        return outOfControl;
    }
    public void setOutOfControl(boolean outOfControl) {
        if(outOfControl != this.outOfControl) {
            Logger.getInstance().log(String.format("%s out of control: %b", name, outOfControl ));
            this.outOfControl = outOfControl;
        }
    }

    @XmlTransient
    public void setPrevValue(int prevValue) {
        this.prevValue = prevValue;
    }

    @Override
    public String toString(){
        return "Coil: " + name + "\r\n";
    }

    @XmlTransient
    private int value;
    private int prevValue;
    private String name;
}
