package com.sockett;

import com.sockett.common.WindowManager;
import com.sockett.util.ResourceUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Main extends Application {
    //存储ip和cChannelHandlerContext的容器
    public static ConcurrentMap<Integer, ChannelHandlerContext> socketChannelMap = new ConcurrentHashMap<>();
    //客户端组（全部的客户端）
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //开启端口和设备code相关联
    public static ConcurrentMap<Integer, String> port2code = new ConcurrentHashMap<>();
    //打印窗口全局定义
    public static TextArea globalTextArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowManager.setPrimaryStage(primaryStage);
        WindowManager.mainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
