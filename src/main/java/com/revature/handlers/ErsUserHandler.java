package com.revature.handlers;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.requests.NewUpdateRequest;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.dtos.requests.UserAuthRequest;
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
import java.sql.SQLException;
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


            logger.info("Attempting to sign up..");
            ErsUser createdUser;
            if (ersUserService.isValidUsername(req.getUsername())) {
                if (!ersUserService.isDuplicateUsername(req.getUsername())) {
                    if (ersUserService.isValidPassword(req.getPassword1())) {
                        if (ersUserService.isSamePassword(req.getPassword1(), req.getPassword2())) {
                            createdUser = ersUserService.saveErsUser(req);
                        } else throw new InvalidUserException("Passwords do not match");
                    } else throw new InvalidUserException("Password needs to be minimum 8 characters long, and one number");
                } else throw new InvalidUserException("Username is already taken");
            } else throw new InvalidUserException("Username needs to be 8 - 20 characters long");
            c.status(201); // CREATED
            c.json(createdUser.getUserID());
            logger.info("Signup attempt successful.");
        } catch (InvalidUserException e) {
            c.status(403); // FORBIDDEN
            c.json(e);
            logger.info("Sign up unsuccessful..");
        }



    }
    public void getAllUsers(Context ctx) {

        try {
            String token = ctx.req.getHeader("authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            //logger.info(principal.toString());
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals("3")) throw new InvalidAuthException("You are not authorized to do this");

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
    public void updateUser(Context ctx) {
        try {
            UserAuthRequest req = mapper.readValue(ctx.req.getInputStream(), UserAuthRequest.class);
            String token = ctx.req.getHeader("Authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");

            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals("3")) throw new InvalidAuthException("You are not authorized to do this");

            ersUserService.updateUser(req);


        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
