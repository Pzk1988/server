package com.homeAutomation.Web.Controller;

import com.homeAutomation.Server.Logger.Logger;
import com.homeAutomation.Server.ServerMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class testController {

    @Autowired
    private ServerMain serverMain;

    @RequestMapping("/Alarm/{value}")
    public String test(@PathVariable int value){

        serverMain.setCoil("Alarm", value);
        return "test";
    }

    @PostMapping(value="/Alarm")
    public String test1(@RequestBody Hash hash){
        Logger.getInstance().log("Alarm put request: " + hash.id);
        //serverMain.setCoil("Alarm", value);
        return "test";
    }
}
