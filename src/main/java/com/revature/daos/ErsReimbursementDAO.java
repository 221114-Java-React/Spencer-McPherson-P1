package com.revature.daos;

import com.revature.models.ErsReimbursement;
import com.revature.models.ErsUser;
import com.revature.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//hold all values for object from db

public class ErsReimbursementDAO implements CrudDAO<ErsReimbursement> {
    @Override
    public void save(ErsReimbursement obj) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursements(reimb_id,amount,submitted,resolved,description,receipt,payment_id,author_id,resolver_id,status_id,type_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, obj.getReimb_id());
            ps.setDouble(2, obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4, null);
            ps.setString(5, obj.getDescription());
            ps.setBytes(6, null);
            ps.setString(7, obj.getPayment_id());
            ps.setString(8, obj.getAuthor_id());
            ps.setString(9, null);
            ps.setString(10, obj.getStatus_id());
            ps.setString(11, obj.getType_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateA(ErsReimbursement obj) throws SQLException {
        ErsReimbursement ticketUpdate = new ErsReimbursement();
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE ers_reimbursements SET resolved = ?,resolver_id=? ,status_id=?  WHERE reimb_id= ?");
            ps.setTimestamp(1, obj.getResolved());
            ps.setString(2, obj.getResolver_id());
            ps.setString(3, obj.getStatus_id());
            ps.setString(4, obj.getReimb_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public String findByType(String type) {
        String typeId = "";
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (type_id) FROM ers_reimbursements_types WHERE type_id = ?");
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                typeId = rs.getString("type_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeId;
    }



    public static List<ErsReimbursement> findAll() {
        List<ErsReimbursement> tickets = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ErsReimbursement currentTicket = new ErsReimbursement(rs.getString("reimb_id"), rs.getDouble("amount"), rs.getTimestamp("submitted"), rs.getTimestamp("resolved"), rs.getString("description"), rs.getBytes("receipt"), rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"), rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(currentTicket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }


    public static ErsReimbursement findAllById(String reimb_id) {
        ErsReimbursement ticket = new ErsReimbursement();
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT FROM ers_reimbursements WHERE reimb_id LIKE ?");
            ps.setString(1,reimb_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                  ticket = new ErsReimbursement(rs.getString("reimb_id"), rs.getDouble("amount"), rs.getTimestamp("submitted"), rs.getTimestamp("resolved"), rs.getString("description"), rs.getBytes("receipt"), rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"), rs.getString("status_id"), rs.getString("type_id"));

            }
//            System.out.println(ticket);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public String findByStatusId(String status_id) {
        String status = "";
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (status_id) FROM ers_reimbursements_statuses WHERE status = ?");
            ps.setString(1, status_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                status = rs.getString("reimb_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static List<String> getByStatusId() {
        List<String> reimb_ids = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (reimb_id) from ers_reimbursements WHERE status_id LIKE '1'");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String currentreimb_id = rs.getString("reimb_id");
                reimb_ids.add(currentreimb_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reimb_ids;

    }

    public static List<ErsReimbursement> findAllPending() {
        List<ErsReimbursement> tickets = new ArrayList<>();

        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements WHERE status_id LIKE ?");
            ps.setString(1,"1%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ErsReimbursement currentTicket = new ErsReimbursement(rs.getString("reimb_id"), rs.getDouble("amount"), rs.getTimestamp("submitted"), rs.getTimestamp("resolved"), rs.getString("description"), rs.getBytes("receipt"), rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"), rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(currentTicket);

            }

            }catch(SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    public static List<ErsReimbursement> findAllTickets(String author_id) {
        List<ErsReimbursement> tickets = new ArrayList<>();

        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements WHERE author_id = ?");
            ps.setString(1,author_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ErsReimbursement currentTicket = new ErsReimbursement(rs.getString("reimb_id"), rs.getDouble("amount"), rs.getTimestamp("submitted"), rs.getTimestamp("resolved"), rs.getString("description"), rs.getBytes("receipt"), rs.getString("payment_id"), rs.getString("author_id"), rs.getString("resolver_id"), rs.getString("status_id"), rs.getString("type_id"));
                tickets.add(currentTicket);

            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }


    @Override
    public void delete(ErsReimbursement obj) {

    }

    @Override
    public void update(ErsReimbursement obj) throws SQLException {

    }

    @Override
    public ErsReimbursement findById() {
        return null;
    }


}