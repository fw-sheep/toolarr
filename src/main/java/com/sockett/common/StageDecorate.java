package com.sockett.common;

import com.sockett.controller.TitleBarController;
import com.sockett.util.ResourceUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class StageDecorate {

    public static void setTitleBar(Stage stage, Parent content, String title, Image icon, int height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(ResourceUtil.getFxml("title_bar"));
            fxmlLoader.load();
            TitleBarController titleBarController = fxmlLoader.getController();
            titleBarController.setTitle(title);
            titleBarController.setStage(stage, height);
            titleBarController.setContent(content);
            Scene scene = new Scene((Parent) fxmlLoader.getRoot());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
