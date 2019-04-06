package com.homeAutomation.Server.Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.AbstractList;
import java.util.ArrayList;

@XmlRootElement(name="InfixExpressions")
public class InfixExpressions extends AbstractList<InfixExpression> {
    @XmlElements({
            @XmlElement(name = "InfixExpressions", type = InfixExpression.class, required = false)
    })
    private  final ArrayList<InfixExpression> list = new ArrayList();

    @Override
    public InfixExpression get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(InfixExpression infixExp){
        return list.add(infixExp);
    }

    @Override
    public String toString(){
        String res = "";
        for(InfixExpression exp : list){
            res += exp.toString();
        }
        return  res;
    }
}
