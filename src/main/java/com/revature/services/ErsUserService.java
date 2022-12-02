package com.revature.services;

import com.revature.daos.ErsUserDAO;
import com.revature.dtos.requests.NewLoginRequest;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.dtos.responses.Principal;
import com.revature.models.ErsUser;
import com.revature.utils.custom_exceptions.InvalidAuthException;
import com.revature.utils.custom_exceptions.InvalidUserException;

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


    public void saveErsUser (NewUserRequest req) {
        List<String> usernames= ersUserDAO.findAllUsernames();
        if (!isValidUsername(req.getUsername())) throw new InvalidUserException("Username needs to be 8-20 characters long");
        if (usernames.contains(req.getUsername())) throw new InvalidUserException("Username is already taken :(");
        if (!isValidPassword(req.getPassword1())) throw new InvalidUserException("Password needs to be minimum eight characters, at least one letter and one number");
        if (!req.getPassword1().equals(req.getPassword2())) throw new InvalidUserException("Passwords do not match :(");
        ErsUser createdUser = new ErsUser(UUID.randomUUID().toString(),req.getUsername(),req.getEmail(),req.getPassword1(),req.getGiven_name(),req.getSurname(),false,"1");
        ersUserDAO.save(createdUser);
    }

    public Principal login(NewLoginRequest req) {
        ErsUser validUser = ErsUserDAO.getUserByUsernameAndPassword(req.getUsername(), req.getPassword());
        if (validUser==null) {
            throw new InvalidAuthException("Invalid username or password");
        }
        Principal principal = new Principal(validUser.getUserID(), validUser.getUsername(), validUser.getEmail(), validUser.getGiven_name(), validUser.getIs_Active(), validUser.getRole());
        return principal;

    }
    private boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");

    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    }

}
