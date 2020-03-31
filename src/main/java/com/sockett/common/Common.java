package com.sockett.common;

public class Common {

    public enum DatabaseType{

        mysql("mysql"),
        oracle("oracle"),
        ;
        public String value;

        DatabaseType(String value) {
            this.value = value;
        }
    }

}
