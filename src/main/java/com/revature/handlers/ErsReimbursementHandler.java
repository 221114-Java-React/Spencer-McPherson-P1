package com.revature.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.ErsReimbursementDAO;
import com.revature.dtos.requests.NewTicketRequest;
import com.revature.dtos.requests.NewUpdateRequest;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsReimbursement;
import com.revature.models.ErsUser;
import com.revature.services.ErsReimbursementService;
import com.revature.services.ErsUserService;
import com.revature.services.TokenService;
import com.revature.utils.TokenValidation;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import com.revature.utils.custom_exceptions.InvalidTicketException;
import com.revature.utils.custom_exceptions.InvalidUserException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
            if (!TokenValidation.isEmployeeToken(token,tokenService)) throw new InvalidAuthException("Only employees can make tickets");
            String author_id = TokenValidation.getOwner(token,tokenService);
            ersReimbursementService.saveTicket(req, author_id);
            ctx.status(201); // CREATED

        } catch (InvalidTicketException e) {
            ctx.status(401); // FORBIDDEN
            ctx.json(e);
        }


    }

    public void processTicketA(Context ctx) throws IOException {
        NewUpdateRequest req = mapper.readValue(ctx.req.getInputStream(),NewUpdateRequest.class);
        try{
            String token = ctx.req.getHeader("authorization");
            String ticketId = ctx.req.getParameter("reimb_id");
            if(!TokenValidation.isManagerToken(token,tokenService)) throw new InvalidAuthException("Manager only request");
            String resolverId = TokenValidation.getOwner(token,tokenService);


            ErsReimbursement updateTicket = ErsReimbursementService.getTicket(ticketId);
            ersReimbursementService.updateTicketA(req,resolverId);
            logger.info("Ticket updated");
            ctx.status(200);
            //ctx.json(updateTicket);
        } catch (InvalidAuthException | SQLException e){
            ctx.status(401);
            ctx.json(e);
        }


    }
    public void getAllPending(Context ctx) throws IOException {
        try{
            String token = ctx.req.getHeader("authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");

            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals("2")) throw new InvalidAuthException("You are not authorized to do this");

            List<ErsReimbursement> tickets = ErsReimbursementService.fetchAllPending();
            ctx.json(tickets);
        } catch(InvalidAuthException e) {
            e.printStackTrace();
            ctx.status(401);
            ctx.json(e);
        }

    }

    public void getAllTickets(Context ctx) throws IOException {
        try {
            String token = ctx.req.getHeader("authorization");
            if (token == null || token.isEmpty()) throw new InvalidAuthException("You are not signed in");

            Principal principal = tokenService.extractRequesterDetails(token);
            if (principal == null) throw new InvalidAuthException("Invalid token");
            if (!principal.getRole().equals("1")) throw new InvalidAuthException("You are not authorized to do this");

            List<ErsReimbursement> tickets = ErsReimbursementService.fetchAllTickets(principal.getUserID());
            ctx.json(tickets);
        } catch (InvalidAuthException e) {
            ctx.status(401);
            ctx.json(e);
        }
    }
    }

