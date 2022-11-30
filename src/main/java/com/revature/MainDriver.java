package com.revature;

import com.revature.utils.ConnectionFactory;
import com.revature.utils.Router;
import io.javalin.Javalin;

import java.sql.SQLException;

public class MainDriver {
    public static void main(String [] args) throws SQLException {
            System.out.println();
            Javalin app = Javalin.create(c-> {
                c.contextPath="/project1";
            }).start(8080);

        Router.router(app);
        }
}
