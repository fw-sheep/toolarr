package com.sockett.controller;

import com.sockett.common.GlobalMenu;
import com.sockett.common.StageMove;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TitleBarController {

    private static final int DOUBLE_CLICK = 2;

    private Stage stage;

    @FXML
    public VBox rootVBox;

    @FXML
    public HBox titleBarHBox;

    @FXML
    public HBox menuBarHBox;

    @FXML
    public Label titleLabel;

    @FXML
    public Button menuButton;

    public void setContent(Parent content) {
        this.rootVBox.getChildren().add(content);
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    public void setStage(Stage stage, int height) {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("文件操作");
//        menuFile.getClass().
        Menu menuTest = new Menu("数据测试");
        Menu menuTrans = new Menu("格式转换");
        Menu menuWeb = new Menu("网页浏览");
        menuBar.getMenus().addAll(menuFile, menuTest, menuTrans, menuWeb);
        menuBarHBox.getChildren().addAll(menuBar);

        this.stage = stage;
        titleBarHBox.setPrefHeight(height);
        new StageMove(this.stage).enableDrag(titleBarHBox);
    }

    /**
     * 关闭按钮点击事件
     */
    @FXML
    public void closeButtonClickAction() {
        stage.close();
    }

//    /**
//     * 窗口最大化事件
//     */
//    @FXML
//    private void maxButtonClickAction() {
//        stage.setMaximized(!stage.isMaximized());
//    }

    /**
     * 窗口最小化事件
     */
    @FXML
    public void minButtonClickAction() {
        stage.setIconified(true);
    }

    /**
     * 窗口菜单按钮点击事件
     */
    @FXML
    public void menuButtonClickAction() {
        GlobalMenu.getInstance().show(menuButton, Side.BOTTOM, 0, 0);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

}
