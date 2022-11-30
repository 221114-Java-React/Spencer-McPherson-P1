package com.revature.handlers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.requests.NewLoginRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsUser;
import com.revature.services.ErsUserService;
import com.revature.services.TokenService;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//purpose of this class is to authenticate user
public class AuthHandler {
//dependecy injection
    private final ErsUserService ersUserService;
    private final ObjectMapper mapper;

    private final TokenService tokenService;
    private static final Logger logger = LoggerFactory.getLogger(ErsUser.class);

    public AuthHandler(ErsUserService ersUserService, ObjectMapper mapper, TokenService tokenService) {
        this.ersUserService = ersUserService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void authenticateUser(Context ctx) throws IOException {
        NewLoginRequest req = mapper.readValue(ctx.req.getInputStream(),NewLoginRequest.class);
        logger.info("Attempting to login..");
        try {
            Principal principal = ersUserService.login(req);
            //generate token from principal obj
            String token = tokenService.generateToken(principal);
            //set header with auth token
            ctx.res.setHeader("authorization",token);
            //return principal obj as json
            ctx.json(principal);

            ctx.status(202);

            logger.info("Login successful.");
        } catch(InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        }
    }
}
