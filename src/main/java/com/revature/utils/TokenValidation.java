package com.revature.utils;


import com.revature.dtos.responses.Principal;
import com.revature.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenValidation {
    private static final Logger logger = LoggerFactory.getLogger(TokenValidation.class);

    //Checks for valid employee priviledges
    public static boolean isEmployeeToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("1")) {
            logger.info("Owner is not a employee");
            return false;
        }
        return true;
    }

    //Checks for valid manager priviledges
    public static boolean isManagerToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("2")) {
            logger.info("Owner is not a manager");
            return false;
        }
        return true;
    }

    //Checks for valid admin priviledges
    public static boolean isAdminToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("3")) {
            logger.info("Owner is not a admin");
            return false;
        }
        return true;
    }

    //Gets the Token's Owner's ID
    public static String getOwner(String token, TokenService tokenService){
        Principal principal = tokenService.extractRequesterDetails(token);
        String id = principal.getUserID();
        if (id == null || id.equals("")) return "";
        return id;
    }

    //Gets the Token's Owner's Role
    public static String getOwnerRole(String token, TokenService tokenService){
        Principal principal = tokenService.extractRequesterDetails(token);
        String role = principal.getRole();
        if (role == null || role.equals("")) return "";
        return role;
    }

    public static boolean isEmptyToken(String token){
        if (token == null || token.isEmpty()) {
            logger.info("No token detected");
            return true;
        } else {
            return false;
        }
    }
}
