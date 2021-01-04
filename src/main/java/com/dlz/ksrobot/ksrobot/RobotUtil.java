package com.dlz.ksrobot.ksrobot;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;



public class RobotUtil {

    private Robot robot;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public RobotUtil(){
        try {
            robot=new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public RobotUtil(Robot robot){
        this.robot = robot;
    }

    /**
     * 输入Key
     */
    public void input(List<Integer> keys){
        keys.stream().forEach(key -> {
            robot.keyPress(key.intValue());
            robot.delay(50);
        });
        keys.stream().forEach(key -> {
            robot.keyRelease(key.intValue());
        });
    }

    /**
     * 移动鼠标
     * @param x
     * @param y
     */
    public void mouseMovement(Double x,Double y){
        int width = screenSize.width;
        int height = screenSize.height;
        robot.mouseMove(x.intValue(),y.intValue());
    }

    // 鼠标点击事件
    public void clickMouse(int key){
        switch (key) {
            case 1:
                robot.mousePress(InputEvent.BUTTON1_MASK); //按下左键
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON1_MASK); //松开左键
                break;
            case 2:
                robot.mousePress(InputEvent.BUTTON2_MASK); //按下滚轴键
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON2_MASK); //松开滚轴键
                break;
            case 3:
                robot.mousePress(InputEvent.BUTTON3_MASK); //按下右键
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON3_MASK); //松开右键
                break;
            default:
                robot.mousePress(InputEvent.BUTTON1_MASK); //按下左键
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON1_MASK); //松开左键
                break;
        }
    }

    public void input(Integer key){
            robot.keyPress(key);
            robot.delay(50);
            robot.keyRelease(key);
    }
    public BufferedImage screenshot(){
         return robot.createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height));
    }
}
