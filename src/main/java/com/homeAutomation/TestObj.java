package com.homeAutomation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//@Scope("signletone")
@Component
public class TestObj {
    private static int id = 0;

    public int getId(){
        return  id++;
    }
}
