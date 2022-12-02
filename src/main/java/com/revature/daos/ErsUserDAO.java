package com.revature.daos;

import com.revature.models.ErsUser;
import com.revature.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ErsUserDAO implements CrudDAO<ErsUser> {


    /* purpose of UserDAO is to return data from the database */
    /* DAO = DATA ACCESS OBJECT */
    @Override
    public void save(ErsUser obj) {
        try(Connection con= ConnectionFactory.getInstance().getConnection()) {
            /* always start with the PrepareStatement */
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_users(user_id,username,email,password,given_name,surname,is_active,role_id) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1,obj.getUserID());
            ps.setString(2,obj.getUsername());
            ps.setString(3,obj.getEmail());
            ps.setString(4,obj.getPassword());
            ps.setString(5,obj.getGiven_name());
            ps.setString(6,obj.getSurname());
            ps.setBoolean(7, obj.getIs_Active());
            ps.setString(8,obj.getRole());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(ErsUser obj) {
        //log out

    }

    @Override
    public void update(ErsUser obj) {

    }

    @Override
    public ErsUser findById() {
        return null;
    }


    public static List<ErsUser> findAll() {
        List<ErsUser> users = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * from ers_users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ErsUser currentUser = new ErsUser(rs.getString("user_id"),rs.getString("username"),rs.getString("email"),rs.getString("password"),
                        rs.getString("given_name"),rs.getString("surname"),rs.getBoolean("is_active"),rs.getString("role_id"));
                users.add(currentUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;

    }

    public List<String> findAllUsernames() {
        List<String> usernames = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT (username) from ers_users");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String currentUsername = rs.getString("username");
                usernames.add(currentUsername);

            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public static ErsUser getUserByUsernameAndPassword(String username, String password) {
        ErsUser user = null;
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users WHERE username=? AND password=?");
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                user = new ErsUser(rs.getString("user_id"),rs.getString("username"),rs.getString("email"),rs.getString("password"),
                        rs.getString("given_name"),rs.getString("surname"),rs.getBoolean("is_active"),rs.getString("role_id"));
            }
        }
        catch(SQLException e)  {
            e.printStackTrace();
        }
        return user;
    }

    public static List<ErsUser> getAllUsersByUsername(String username) {
        List<ErsUser> users = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users WHERE username LIKE ?");
            ps.setString(1, username + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ErsUser user = new ErsUser(rs.getString("user_id"),rs.getString("username"),rs.getString("email"),rs.getString("password"),
                        rs.getString("given_name"),rs.getString("surname"),rs.getBoolean("is_active"),rs.getString("role_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
