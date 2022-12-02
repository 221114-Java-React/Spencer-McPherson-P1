package com.revature.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.requests.NewTicketRequest;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsReimbursement;
import com.revature.models.ErsUser;
import com.revature.services.ErsReimbursementService;
import com.revature.services.ErsUserService;
import com.revature.services.TokenService;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import com.revature.utils.custom_exceptions.InvalidTicketException;
import com.revature.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ErsReimbursementHandler {
    //takes from service
    //all functions here will become endpoints

    //reimb handler > reimb service > reimb dao > db

    private final ErsReimbursementService ersReimbursementService;

    private final ObjectMapper mapper;
    private final TokenService tokenService;
    private final static Logger logger = LoggerFactory.getLogger(ErsReimbursement.class);

    public ErsReimbursementHandler(ErsReimbursementService ersReimbursementService, ObjectMapper mapper, TokenService tokenService) {
        this.ersReimbursementService = ersReimbursementService;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public void newTicket(Context ctx) throws IOException {
        NewTicketRequest req = mapper.readValue(ctx.req.getInputStream(), NewTicketRequest.class);
        try {
            String token = ctx.req.getHeader("authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
            Principal principal = tokenService.extractRequesterDetails(token);
            logger.info(principal.toString());
            if (principal == null) throw new InvalidAuthException("Invalid token");
            ersReimbursementService.saveTicket(req, principal.getUserID(), req.getType_id(), req.getStatus_id());
            ctx.status(201); // CREATED

        } catch (InvalidTicketException e) {
            ctx.status(403); // FORBIDDEN
            ctx.json(e);
        }


    }

    public void processTicketA(Context ctx) {
        List<ErsReimbursement> ticketList = ErsReimbursementService.getAllTickets();
            try {
                String token = ctx.req.getHeader("authorization");
                if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");
                Principal principal = tokenService.extractRequesterDetails(token);
                for (ErsReimbursement ticket : ticketList) {
                    ersReimbursementService.updateTicketA(ticket);
                    ctx.status(201);
                }
            } catch (InvalidTicketException e) {
                ctx.status(403); // FORBIDDEN
                ctx.json(e);
            }

        }
    }

