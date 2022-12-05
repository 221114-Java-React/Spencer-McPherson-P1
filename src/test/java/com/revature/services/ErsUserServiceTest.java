package com.revature.services;

import com.revature.daos.ErsUserDAO;
import com.revature.dtos.requests.NewUserRequest;
import com.revature.models.ErsUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ErsUserServiceTest {
private ErsUserService sut;
private final ErsUserDAO mockUserDAO = Mockito.mock(ErsUserDAO.class);



    @Before
    public void init() {

        sut= new ErsUserService(mockUserDAO);

    }

@Test
    public void test_isValidUsername_givenCorrectUsername() {
    //Arrange
    String validUsername = "smac98004";

    //Act
    boolean condition = sut.isValidUsername(validUsername);

    //Assert
    assertTrue(condition);

}
@Test
    public void test_isValidUsername_givenUniqueUsername() {
        ErsUserService spySut = Mockito.spy(sut);
        String uniqueUsername = "tester004";
        List<String> usernames = Arrays.asList("tester001","tester002","tester003");
        // controlled env
        Mockito.when(mockUserDAO.findAllUsernames()).thenReturn(usernames);

        // Act
        boolean condition = spySut.isDuplicateUsername(uniqueUsername);

        // Assert
        assertFalse(condition);

    //Act

    //Assert
    assertFalse(condition);
}
    @Test
    public void test_isValidSignup_persistUserGivenUsernameAndPassword() {
        // Arrange
        ErsUserService spySut = Mockito.spy(sut);
        String validUsername = "michael007";
        String validPassword1 = "passw0rd";
        String validPassword2 = "passw0rd";
        String email ="yahoo@gmail0001.com";
        String given_name= "spencer";
        String surname = "McPherson";
        String role="EMPLOYEE";
        NewUserRequest stubbedReq = new NewUserRequest(validUsername, validPassword1, validPassword2,email,given_name,surname,role);

        // Act
        ErsUser createdUser = spySut.saveErsUser(stubbedReq);

        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUserID());
        assertNotNull(createdUser.getUsername());
        assertNotNull(createdUser.getPassword());
        assertNotNull(createdUser.getEmail());
        assertNotNull(createdUser.getGiven_name());
        assertNotNull(createdUser.getSurname());
        assertNotNull(createdUser.getRole());
        assertNotEquals("", createdUser.getUsername());
        Mockito.verify(mockUserDAO, Mockito.times(1)).save(createdUser);
    }



}