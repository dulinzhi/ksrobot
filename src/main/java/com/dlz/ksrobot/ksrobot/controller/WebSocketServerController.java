package com.dlz.ksrobot.ksrobot.controller;

import com.dlz.ksrobot.ksrobot.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class WebSocketServerController {

    @Resource
    private WebSocketServer webSocketServer;

    @RequestMapping("/listOfPeople")
    @ResponseBody
    public List<String> listOfPeople(){
        return webSocketServer.listOfPeople();
    }
}
