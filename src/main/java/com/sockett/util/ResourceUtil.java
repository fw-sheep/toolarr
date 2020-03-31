package com.sockett.util;


import javafx.scene.image.Image;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public final class ResourceUtil {

    public static URL getResource(String resourceFile){
        resourceFile = FileUtil.filterFileSeparator(resourceFile);
        return ResourceUtil.class.getClassLoader().getResource(resourceFile);
    }

    public static URL getFxml(String fxmlName){
        String suffix = ".fxml";
        if(!fxmlName.toLowerCase().endsWith(suffix)){
            fxmlName = fxmlName + suffix;
        }
        String resourceFile =  "fxml"+ File.separator + fxmlName;
        return getResource(resourceFile);
    }
    public static Image getHtml(String fxmlName){
        String suffix = ".html";
        if(!fxmlName.toLowerCase().endsWith(suffix)){
            fxmlName = fxmlName + suffix;
        }
        String resourceFile =  "html"+ File.separator + fxmlName;
        return new Image(getResource(resourceFile).getFile());
    }

    public static InputStream getResourceAsStream(String resourceFile){
        resourceFile = FileUtil.filterFileSeparator(resourceFile);
        return ResourceUtil.class.getClassLoader().getResourceAsStream(resourceFile);
    }
}
