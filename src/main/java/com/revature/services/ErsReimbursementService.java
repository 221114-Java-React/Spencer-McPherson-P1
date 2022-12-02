package com.revature.services;

import com.revature.daos.ErsReimbursementDAO;
import com.revature.dtos.requests.NewTicketRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsReimbursement;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ErsReimbursementService {
    //takes data from dao and validates
    private final ErsReimbursementDAO ersReimbursementDAO;

    public ErsReimbursementService(ErsReimbursementDAO ersReimbursementDAO) {
        this.ersReimbursementDAO = ersReimbursementDAO;
    }
    public static List<ErsReimbursement> getAllTickets() {
        return ErsReimbursementDAO.findAll();
    }

    //String reimb_id, String amount, String submitted, String resolved, String description, String receipt, String payment_id, String author_id, String resolver_id, String status_id, String type_id
    public void saveTicket(NewTicketRequest req, String author_id,String type_id,String status_id) {


        ErsReimbursement createdTicket = new ErsReimbursement(UUID.randomUUID().toString(),req.getAmount(), Timestamp.from(Instant.now()),null,req.getDescription(),null,null,author_id,null,status_id,type_id);
        ersReimbursementDAO.save(createdTicket);
    }

    public void updateTicketA(ErsReimbursement ticket) {
        ErsReimbursement updateTicket = new ErsReimbursement(ticket.getReimb_id(), ticket.getAmount(),ticket.getSubmitted(),ticket.getResolved(),ticket.getDescription(),ticket.getReceipt(),ticket.getPayment_id(),ticket.getAuthor_id(), ticket.getResolver_id(), "2", ticket.getType_id());

    }
    public void updateTicketD(ErsReimbursement ticket) {
        ErsReimbursement updateTicket = new ErsReimbursement(ticket.getReimb_id(), ticket.getAmount(),ticket.getSubmitted(),ticket.getResolved(),ticket.getDescription(),ticket.getReceipt(),ticket.getPayment_id(),ticket.getAuthor_id(), ticket.getResolver_id(), "3", ticket.getType_id());

    }

}
