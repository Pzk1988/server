package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.AbstractList;
import java.util.ArrayList;

@XmlRootElement(name="Coils")
public class Coils extends AbstractList<Coil> {
    @XmlElements({
            @XmlElement(name = "Coil", type = Coil.class, required = false)
    })
    private  final AbstractList<Coil> list = new ArrayList();

    @Override
    public Coil get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(Coil coil){
        return list.add(coil);
    }

    @Override
    public String toString(){
        String res = "";
        for(Coil coil : list){
            res += coil.toString();
        }
        return  res;
    }

    public void updatePrevValue() {
        for(Coil coil : list){
            coil.setPrevValue(coil.getValue());
        }
    }
}
