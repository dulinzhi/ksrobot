/*
package com.dlz.ksrobot.ksrobot.controller;

import com.alibaba.fastjson.JSONObject;
import com.dlz.ksrobot.ksrobot.RobotUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class t1Controller {

    @Resource
    RobotUtil robotUtil;

    @RequestMapping("/t2")
    public String t2(String msg){

        JSONObject.parse(msg);

        return null;
    }

    @RequestMapping("/t1")
    public String createGrahp(){
       BufferedImage bufferedImage = robotUtil.screenshot();
       long currentTime = System.currentTimeMillis();

       String fileName = this.getClass().getResource("/").getPath() +
               "static/images/"+currentTime +".jpg";
       try {
           ImageIO.write(bufferedImage,"jpg",new File(fileName));
       } catch (IOException e) {
           e.printStackTrace();
       }
       return "/static/images/"+currentTime+".jpg";
    }

    @RequestMapping("/input")
    public void input(Integer key){
        robotUtil.input(key);
    }

    @RequestMapping("/mouseMovement")
    public void mouseMovement(Double x,Double y){
        robotUtil.mouseMovement(x,y);
    }

    @RequestMapping("/clickMouse")
    public void clickMouse(Integer key){
        robotUtil.clickMouse(key);
    }



}
*/
