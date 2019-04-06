package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="Outputs")
public class Outputs extends AbstractList<Output> {

    @XmlElements({
            @XmlElement(name = "DigitalOutput", type = Output.class, required = false)
    })
    private  final AbstractList<Output> list = new ArrayList();

    @Override
    public Output get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(Output output){
        return list.add(output);
    }

    @Override
    public String toString(){
        String res = "";
        for(Output output: list){
            res += output.toString() + "\r\n";
        }
        return  res;
    }

    public void updatePrevValue() {
        for(Output out : list){
            out.setPrevValue(out.getValue());
        }
    }

    public List<Output> getList(int chassi) {
        int start = getStartIndex(chassi);
        int end = getEndIndex(chassi);
        return list.subList(start, end);
    }

    private int getStartIndex(int chassi) {
        for(int i = 0; i < list.size(); i++){
            if(((chassi * 10000) < list.get(i).getNumber()) ||  (list.get(i).getClass() != Output.class)){
                return i;
            }
        }
        return list.size();
    }

    private int getEndIndex(int chassi) {
        for(int i = 0; i < list.size(); i ++){
            if((list.get(i).getNumber() > ((chassi * 10000) + 9999)) || (list.get(i).getClass() != Output.class)){
                return i;
            }
        }
        return list.size();
    }
}
