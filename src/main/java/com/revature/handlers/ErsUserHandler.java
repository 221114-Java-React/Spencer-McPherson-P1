package com.revature.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsUser;
import com.revature.services.ErsUserService;
import com.revature.services.TokenService;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import com.revature.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.List;

public class ErsUserHandler {
    /* purpose of this UserHandler class is to handle http verbs and endpoints */
    /* hierarchy dependency injection -> userhandler -> userservice -> userdao */
    private final ErsUserService ersUserService;
    private final ObjectMapper mapper;
    private final static Logger logger = LoggerFactory.getLogger(ErsUser.class);
    private final TokenService tokenService;

    public ErsUserHandler(ErsUserService ersUserService, ObjectMapper mapper, TokenService tokenService) {
        this.ersUserService = ersUserService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void signup(Context c) throws IOException {
        NewUserRequest req = mapper.readValue(c.req.getInputStream(), NewUserRequest.class);
        try {
            ersUserService.saveErsUser(req);
            c.status(201); // CREATED

        } catch (InvalidUserException e) {
            c.status(403); // FORBIDDEN
            c.json(e);
        }



    }
    public void getAllUsers(Context ctx) {

        try {
            String token = ctx.req.getHeader("authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            logger.info(principal.toString());
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals("2")) throw new InvalidAuthException("You are not authorized to do this");

            List<ErsUser> users = ErsUserService.getAllUsers();

            // this is a response
            ctx.json(users);
        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        }
    }
    public void getAllUsersByUsername(Context ctx) {
        try {
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");

            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals("2")) throw new InvalidAuthException("You are not authorized to do this");

            String username = ctx.req.getParameter("username");
            List<ErsUser> users = ErsUserService.getAllUsersByUsername(username);
            ctx.json(users);
        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        }
    }
}
