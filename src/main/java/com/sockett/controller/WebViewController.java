package com.sockett.controller;

import com.sockett.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {
    @FXML
    private WebView webViewIndex;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        final WebEngine webengine = webViewIndex.getEngine();
        String url = Main.class.getResource("/html/diff.html").toExternalForm();
        webengine.load(url);
    }
}
