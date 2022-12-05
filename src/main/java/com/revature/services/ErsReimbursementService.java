package com.revature.services;

import com.revature.daos.ErsReimbursementDAO;
import com.revature.dtos.requests.NewTicketRequest;
import com.revature.dtos.requests.NewUpdateRequest;
import com.revature.models.ErsReimbursement;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import com.revature.utils.custom_exceptions.InvalidTicketException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ErsReimbursementService {
    //takes data from dao and validates
    private  final ErsReimbursementDAO ersReimbursementDAO;

    public ErsReimbursementService(ErsReimbursementDAO ersReimbursementDAO) {
        this.ersReimbursementDAO = ersReimbursementDAO;
    }
    public static List<ErsReimbursement> getAllTickets() {

        return ErsReimbursementDAO.findAll();
    }

    public static ErsReimbursement getTicket(String id) {
        return  ErsReimbursementDAO.findAllById(id);
    }

    public void saveTicket(NewTicketRequest req, String author_id) {
        String type_id = ersReimbursementDAO.findByType(req.getType_id());
        ErsReimbursement createdTicket = new ErsReimbursement(UUID.randomUUID().toString(),req.getAmount(), Timestamp.from(Instant.now()),null,req.getDescription(),null,null,author_id,null,"1", req.getType_id());
        ersReimbursementDAO.save(createdTicket);
    }

    public void updateTicketA(NewUpdateRequest req,String resolver_id) throws SQLException {
        List<String> list = ErsReimbursementDAO.getByStatusId();
        long time = System.currentTimeMillis();
        Timestamp timeNow = new Timestamp(time);

        if (!list.contains(req.getReimb_id())) {
            throw new InvalidAuthException("Ticket has already been processed or ticket has wrong id");

        }
//        String status_id = ersReimbursementDAO.findByStatusId(req.getStatus_id());
        ErsReimbursement updateTicket = new ErsReimbursement(req.getReimb_id(),timeNow,resolver_id, req.getStatus_id());
        ErsReimbursementDAO.updateA(updateTicket);


    }

    public static List<ErsReimbursement> fetchAllPending() {
        return ErsReimbursementDAO.findAllPending();
    }

    public static List<ErsReimbursement> fetchAllTickets(String author_id) {
        return ErsReimbursementDAO.findAllTickets(author_id);
    }



}
