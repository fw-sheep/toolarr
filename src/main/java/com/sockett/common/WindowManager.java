package com.sockett.common;

import com.sockett.util.ResourceUtil;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class WindowManager {
    private final static int MAIN_TITLE_HEIGHT = 20;

    private static Stage primaryStage;


    public static void mainWindow() {
        primaryStage.setTitle("Server Manager");
        try {
            Parent root = FXMLLoader.load(ResourceUtil.getFxml("lay_out"));
            //隐藏标题栏
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setResizable(false);
            StageDecorate.setTitleBar(primaryStage, root, "内部测试程序1.0", null, MAIN_TITLE_HEIGHT);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        WindowManager.primaryStage = primaryStage;
    }

    private static void setStageOnCenter(Stage stage, double prefWidth, double prefHeight) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primaryScreenBounds.getWidth() - prefWidth) / 2);
        stage.setY((primaryScreenBounds.getHeight() - prefHeight) / 2);
    }
}
