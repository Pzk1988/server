package com.homeAutomation.Server.Model;

import com.homeAutomation.Server.Interface.ILogicExpResult;
import com.homeAutomation.Server.Logger.Logger;

public class Output extends InOutBase implements ILogicExpResult {
    public Output(){}
    public Output(String name){
        super(name);
    }

    @Override
    public void setValue(int value) {
        if(value != this.value) {
            Logger.getInstance().log(String.format("Output: %s, new value: %d", name, value));
            this.value =  value;
        }
    }

    @Override
    public String toString(){
        return "Output: " + name;
    }
}
