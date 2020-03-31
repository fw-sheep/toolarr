package com.sockett.controller;

import com.sockett.socket.NettyClient;
import com.sockett.util.ByteUtil;
import com.sockett.util.ResourceUtil;
import io.netty.buffer.Unpooled;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import static com.sockett.Main.*;

public class SocketTestController {

    @FXML
    public TextField ipInput;
    @FXML
    public TextField portInput;
    @FXML
    public TextField heartInput;
    @FXML
    public TextField mutilNumber;
    @FXML
    public TextField selfText;
    @FXML
    public TextArea logTextArea;
    @FXML
    public Button linkServer;
    @FXML
    public CheckBox heartBeat;
    @FXML
    public CheckBox hexSend;
    @FXML
    public CheckBox openMutil;
    @FXML
    public CheckBox rebootReport;
    @FXML
    public CheckBox accPowerOn;
    @FXML
    public CheckBox accPowerOff;
    @FXML
    public CheckBox serverErr;
    @FXML
    public CheckBox batteryLow;
    @FXML
    public CheckBox senorErr;
    @FXML
    public CheckBox setCode;
    @FXML
    public TextField deviceCode;


    Parent parent = null;
    public static List<Thread> threads = new ArrayList<>();

    @FXML
    public void LinkSever(MouseEvent mouseEvent) {
        try {
            if (globalTextArea == null) {
                globalTextArea = logTextArea;
            }
            if (parent == null) {
                parent = (Parent) FXMLLoader.load(ResourceUtil.getFxml("socket_test.fxml"));
            }
            if (linkServer.getText().equals("断开连接")) {
                for (Thread thread : threads) {
                    thread.interrupt();
                    thread.stop();
                }
                threads.clear();
                linkServer.setText("连接");
                channelGroup.close();
                return;
            }
            String ip = ipInput.getText().trim();
            String port = portInput.getText().trim();
            if ("".equals(ip)) {
                ip = "127.0.0.1";
            }
            boolean b = ByteUtil.isboolIp(ip);
            if (!b) {
                logTextArea.appendText("ip格式不正确\n");
                return;
            }
            if ("".equals(port)) {
                logTextArea.appendText("请输入端口\n");
                return;
            }
            Pattern pattern = Pattern.compile("[0-9]*");
            if (!pattern.matcher(port).matches()) {
                logTextArea.appendText("端口格式不正确\n");
                return;
            }

            //默认不开启多客户端的时候只有一个连接的
            int number = 1;
            String code = "";

            if (setCode.isSelected()) {
                code = deviceCode.getText();
                port2code.put(0, code);
                if (openMutil.isSelected()) {
                    logTextArea.appendText("设置设备编号后，开启多组测试无效！\n");
                }
            } else {
                if (openMutil.isSelected()) {
                    String tn = mutilNumber.getText().trim();
                    if (!"".equals(tn)) {
                        if (!pattern.matcher(tn).matches()) {
                            logTextArea.appendText("调试组数请输入数字\n");
                        } else {
                            number = Integer.valueOf(tn);
                        }
                    }
                }
            }
            NettyClient nc = new NettyClient(ip, Integer.valueOf(port), number);
            nc.start();
            SocketStatusListen ssl = new SocketStatusListen(logTextArea, number, linkServer);
            Thread lookLink = new Thread(ssl);
            lookLink.start();
            threads.add(lookLink);

            if (heartBeat.isSelected()) {
                String heartInp = heartInput.getText().trim();
                if (!"".equals(heartInp)) {
                    if (!pattern.matcher(heartInp).matches()) {
                        logTextArea.appendText("时间间隔请输入数字\n");
                    } else {
                        HeartBeat hbt = new HeartBeat(logTextArea, Integer.valueOf(heartInp), code);
                        Thread heartThread = new Thread(hbt);
                        heartThread.start();
                        threads.add(heartThread);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void sendReport(MouseEvent mouseEvent) {
        try {
            if (rebootReport.isSelected()) {
                sendReport("software_restart");
            }
            if (accPowerOn.isSelected()) {
                sendReport("acc_poweron");
            }
            if (accPowerOff.isSelected()) {
                sendReport("acc_poweroff");
            }
            if (serverErr.isSelected()) {
                sendReport("server_err");
            }
            if (batteryLow.isSelected()) {
                sendReport("battery_low");
            }
            if (senorErr.isSelected()) {
                sendReport("sensor");
            }
        } catch (Exception e) {
            System.out.println("***********");
        }
    }

    public void sendReport(String type) {
        String code = "";
        if (port2code.size() > 0) {
            for (Integer port : port2code.keySet()) {
                code = port2code.get(port);
                break;
            }
        }

        if (socketChannelMap.size() > 0) {
            for (Integer port : socketChannelMap.keySet()) {
                String msg = "start," + type + "," + ("".equals(code) ? port : code) + ",end";
                socketChannelMap.get(port).writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
                logTextArea.appendText("<---：localhost:" + port + ":向服务器发送了" + type + "信息" + msg + " \n");
            }
        }
    }

    @FXML
    public void sendMsg(MouseEvent mouseEvent) {
        if (socketChannelMap.size() == 0) {
            logTextArea.appendText("当前没有设备连接，不能发送信息！\n");
            return;
        }
        String msg = selfText.getText().trim();
        logTextArea.appendText("将要发送的信息是：" + msg + "\n");
        try {

            if (hexSend.isSelected()) {
                channelGroup.writeAndFlush(Unpooled.copiedBuffer(ByteUtil.toBytes(msg)));
            } else {
                channelGroup.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
            }
        } catch (Exception e) {

        }

    }

    @FXML
    public void cleantext(MouseEvent mouseEvent) {
        logTextArea.clear();
    }

    public class SocketStatusListen implements Runnable {
        private int number;
        private Button linkButton;
        private TextArea textArea;

        public SocketStatusListen(TextArea textArea, int number, Button linkButton) {
            this.number = number;
            this.linkButton = linkButton;
            this.textArea = textArea;
        }

        @Override
        public void run() {
            int temp = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (socketChannelMap == null) {
                        return;
                    }
                    if (socketChannelMap.size() > 0) {
                        if ((socketChannelMap.size() < number + 1) && (socketChannelMap.size() != temp)) {
                            Platform.runLater(() -> {
                                if (linkButton.getText().equals("连接")) {
                                    linkButton.setText("断开连接");
                                }
                            });
                            temp = socketChannelMap.size();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("*******************:" + e.getMessage());
                }
            }
        }

    }

    public class HeartBeat implements Runnable {
        int heartbeatnum;
        private TextArea textArea;
        private String code;

        public HeartBeat(TextArea textArea, int heartbeatnum, String code) {
            this.code = code;
            this.textArea = textArea;
            this.heartbeatnum = heartbeatnum;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * heartbeatnum);
                    if (socketChannelMap == null) {
                        return;
                    }
                    if (socketChannelMap.size() > 0) {
                        for (Integer port : socketChannelMap.keySet()) {
                            String hb = "start,plus," + ("".equals(code) ? port : code) + ",time,end";
                            socketChannelMap.get(port).writeAndFlush(Unpooled.copiedBuffer(hb.getBytes()));
                            textArea.appendText("<--- localhost：" + port + ":向服务器发送了心跳:" + hb + "\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("*******************:" + e.getMessage());
                }
            }
        }
    }

    public void linsten(TextArea textArea) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);

                    } catch (Exception e) {

                    }
                }
            }
        });
    }


}
