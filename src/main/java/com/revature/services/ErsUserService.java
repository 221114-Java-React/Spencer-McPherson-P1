package com.revature.services;

import com.revature.daos.ErsUserDAO;
import com.revature.dtos.requests.NewLoginRequest;
import com.revature.dtos.requests.NewUpdateRequest;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.dtos.requests.UserAuthRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsUser;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import com.revature.utils.custom_exceptions.InvalidUserException;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ErsUserService {

    /* purpose of UserService is to validate and retrieve data from the DAO (DATA ACCESS OBJECT) */
    /* Service class is essentially an api */
    private final ErsUserDAO ersUserDAO;

    public ErsUserService(ErsUserDAO ersUserDAO) {
        this.ersUserDAO = ersUserDAO;
    }

    public static List<ErsUser> getAllUsers() {
        return ErsUserDAO.findAll();
    }

    public static List<ErsUser> getAllUsersByUsername(String username) {
        return ErsUserDAO.getAllUsersByUsername(username);
    }

    public static boolean getAllActive(String username) {
        List<String> usernames=  ErsUserDAO.findAllIsActive();
        return usernames.contains(username);
    }

    public static void updateUser(UserAuthRequest req) throws SQLException {
        List<String> list = ErsUserDAO.findAllIsNotActive();
        if(!list.contains(req.getUser_id())) throw new InvalidAuthException("User is already activated");
        ErsUser updated = new ErsUser(req.getUser_id(), null,null,null,null,null,true, req.getRole_id());
        ErsUserDAO.updateU(updated);
    }

    public ErsUser saveErsUser (NewUserRequest req) {
        List<String> usernames= ersUserDAO.findAllUsernames();
        ErsUser createdUser = new ErsUser(UUID.randomUUID().toString(),req.getUsername(),req.getEmail(),req.getPassword1(),req.getGiven_name(),req.getSurname(),false,"1");
        ersUserDAO.save(createdUser);
        return createdUser;
    }

    public Principal login(NewLoginRequest req) {
        ErsUser validUser = ErsUserDAO.getUserByUsernameAndPassword(req.getUsername(), req.getPassword());
        if (validUser==null) {
            throw new InvalidAuthException("Invalid username or password");
        }
        Principal principal = new Principal(validUser.getUserID(), validUser.getUsername(), validUser.getEmail(), validUser.getGiven_name(), validUser.getIs_Active(), validUser.getRole());
        return principal;

    }
    public boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");

    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    }
    public boolean isDuplicateUsername(String username) {
        List<String> usernames = ersUserDAO.findAllUsernames();
        return usernames.contains(username);
    }



    public boolean isSamePassword(String password1, String password2) {
        return password1.equals(password2);
    }
}

