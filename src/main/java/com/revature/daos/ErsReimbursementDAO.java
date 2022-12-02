package com.revature.daos;

import com.revature.models.ErsReimbursement;
import com.revature.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//hold all values for object from db

public class ErsReimbursementDAO implements CrudDAO<ErsReimbursement> {
    @Override
    public void save(ErsReimbursement obj) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursements(reimb_id,amount,submitted,resolved,description,receipt,payment_id,author_id,resolver_id,status_id,type_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1,obj.getReimb_id());
            ps.setBigDecimal(2,obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4, obj.getResolved());
            ps.setString(5,obj.getDescription());
            ps.setBytes(6, obj.getReceipt());
            ps.setString(7, obj.getPayment_id());
            ps.setString(8,obj.getAuthor_id());
            ps.setString(9,obj.getResolver_id());
            ps.setString(10,obj.getStatus_id());
            ps.setString(11,obj.getType_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ErsReimbursement obj) {

    }

    @Override
    public void update(ErsReimbursement obj) throws SQLException {

    }


    public void updateA(ErsReimbursement obj) throws SQLException {
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE ers_reimbursements SET status_id='2' where reimb_id=?");
            ps.setString(1,obj.getReimb_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        }

    @Override
    public ErsReimbursement findById() {
        return null;
    }

    public static List<ErsReimbursement> findAll() {
        List<ErsReimbursement> tickets = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps =con.prepareStatement("SELECT * FROM ers_reimbursements");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ErsReimbursement currentTicket = new ErsReimbursement(rs.getString("reimb_id"),rs.getBigDecimal("amount"),rs.getTimestamp("submitted"),rs.getTimestamp("resolved"),rs.getString("description"),rs.getBytes("receipt"),rs.getString("payment_id"),rs.getString("author_id"),rs.getString("resolver_id"),rs.getString("status_id"),rs.getString("type_id"));
                tickets.add(currentTicket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }
}
