package com.revature.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.ErsReimbursementDAO;
import com.revature.daos.ErsUserDAO;
import com.revature.handlers.AuthHandler;
import com.revature.handlers.ErsReimbursementHandler;
import com.revature.handlers.ErsUserHandler;
import com.revature.services.ErsReimbursementService;
import com.revature.services.ErsUserService;
import com.revature.services.TokenService;
import io.javalin.Javalin;
import org.w3c.dom.UserDataHandler;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {
    //The purpose of this file is to map endpoints
    public static void router(Javalin app) {
        //user
        ErsUserDAO ersUserDAO = new ErsUserDAO();
        ObjectMapper mapper = new ObjectMapper();
        JwtConfig jwtConfig = new JwtConfig();
        TokenService tokenService = new TokenService(jwtConfig);


        ErsUserService ersUserService = new ErsUserService(ersUserDAO);

        ErsUserHandler ersUserHandler = new ErsUserHandler(ersUserService,mapper,tokenService);
        //auth
        AuthHandler authHandler = new AuthHandler(ersUserService,mapper,tokenService);



        //ticket
        ErsReimbursementDAO ersReimbursementDAO = new ErsReimbursementDAO();
        ErsReimbursementService ersReimbursementService = new ErsReimbursementService(ersReimbursementDAO);
        ErsReimbursementHandler ersReimbursementHandler = new ErsReimbursementHandler(ersReimbursementService,mapper,tokenService);


        //handler groups
        //routes > handlers > services > dao
        app.routes(() -> {
            //user
            path("/users", () -> {
                get(c-> ersUserHandler.getAllUsers(c));
                get("/name",ersUserHandler::getAllUsersByUsername);
                post(c -> ersUserHandler.signup(c));
            });

            //auth
            path("auth", () -> {
                post(c -> authHandler.authenticateUser(c));
            });

            //ticket
            path("/ticket", () -> {
               post(c-> ersReimbursementHandler.newTicket(c));
               post("/process",ersReimbursementHandler::processTicketA);
            });

        });
    }
}
