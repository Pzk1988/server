package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.*;

public class TempInput extends Input {

    private String id;
    private long lastActivity;

    public TempInput(){}

    public TempInput(String name, String id) {
        super(name);
        this.id = id;
    }

    @Override
    public String toString(){
        return "Temperature input: " + name + ", id: " + id;
    }
    
    @XmlAttribute
    public String getId(){
        return id;
    }
    public void setId(String id){ this.id = id; }

    @Override
    public synchronized void setValue(int value){
        this.value = value;
    }
    @Override
    public synchronized int getValue(){
        return value;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    public long getLastActivity() {
        return lastActivity;
    }
}
