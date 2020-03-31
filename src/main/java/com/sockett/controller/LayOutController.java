package com.sockett.controller;

import com.sockett.util.ResourceUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LayOutController implements Initializable {
    @FXML
    private TabPane globalTabPan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Parent root = FXMLLoader.load(ResourceUtil.getFxml("socket_test"));
            Parent rootWeb = FXMLLoader.load(ResourceUtil.getFxml("web_view"));
            Tab tab = new Tab("网络测试", root);
            Tab tabWebView = new Tab("页面浏览", rootWeb);
            tab.setClosable(false);
            globalTabPan.getTabs().add(tab);
            globalTabPan.getTabs().add(tabWebView);
        } catch (Exception e) {

        }
    }

}
