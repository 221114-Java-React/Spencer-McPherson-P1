package com.revature;

import io.javalin.Javalin;

public class MainDriver {
    public static void main(String [] args) {
        System.out.println();
        Javalin app = Javalin.create(c-> {
            c.contextPath="/project1";
        }).start(8080);
    }
}
