package com.dlz.ksrobot.ksrobot;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/webSocket/{sid}")
@Component
public class WebSocketServer {

    private static RobotUtil robotUtil;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    public WebSocketServer() {
        robotUtil = new RobotUtil();
    }

    //发送消息
    public void sendMessage(Session session, String message) throws Exception {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                while(true){
                    if(session != null){
                        synchronized (session) {
                            if (session != null) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                byte[] data = new byte[0];
                                try {
                                    data = createImage();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // System.out.println("发送数据：" + message);
                                try {
                                    session.getBasicRemote().sendBinary(ByteBuffer.wrap(data));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        };
        fixedThreadPool.execute(runnable);
    }

    private byte[] createImage()throws Exception{
        BufferedImage bufferedImage =robotUtil.screenshot();
         //创建一段内存流
        ByteArrayOutputStream tempB = new ByteArrayOutputStream();
        //将图片数据写入内存流中
        javax.imageio.ImageIO.write(bufferedImage,"jpeg", tempB);
        //做为字节数组返回
        byte[] data=tempB.toByteArray();
        return data;
    }

    //给指定用户发送信息
    public void sendInfo(String userName, String message){
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> listOfPeople(){
        List<String> users = new ArrayList<>();
        for (Map.Entry<String,Session> in:
        sessionPools.entrySet()) {
            users.add(in.getKey());
        }
        return users;
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String userName){
        sessionPools.put(userName, session);
        addOnlineCount();
        System.out.println(userName + "加入webSocket！当前人数为" + onlineNum);
        try {
            sendMessage(session, "欢迎" + userName + "加入连接！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "sid") String userName){
        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "断开webSocket连接！当前人数为" + onlineNum);
    }
    //收到客户端信息
    @OnMessage
    public void onMessage(String message) throws IOException{
       // message = "客户端：" + message + ",已收到";
        System.out.println(message);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(message);
        if(jsonObject.containsKey("x")){
            robotUtil.mouseMovement(Double.valueOf(jsonObject.get("x").toString()),Double.valueOf(jsonObject.get("y").toString()));
        }
        if(jsonObject.containsKey("mouseClick")){
           // Integer.parseInt(String.valueOf(jsonObject.containsKey("MouseClick")))
            Integer key = Integer.parseInt(String.valueOf(jsonObject.get("mouseClick")));
            if(key < 4){
                robotUtil.clickMouse(key);
            } else {
                robotUtil.ReleaseMouse(key);
            }
        }
        if(jsonObject.containsKey("key")){
            if(Integer.parseInt(jsonObject.get("status").toString())==0){
                robotUtil.input(Integer.parseInt(String.valueOf(jsonObject.get("key"))));
            } else{
                robotUtil.releaseInput(Integer.parseInt(String.valueOf(jsonObject.get("key"))));
            }
        }
      /*
        for (Session session: sessionPools.values()) {
            try {
                sendMessage(session, message);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
        */
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }
    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }
}
