package chatapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    @Test
    public void usernameIsCorrectlyFormatted() {
        Login login = new Login();

        assertEquals(true, login.checkUserName("kyl_1"));
    }

    @Test
    public void usernameIsIncorrectlyFormatted() {
        Login login = new Login();

        assertEquals(false, login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void passwordMeetsComplexityRequirements() {
        Login login = new Login();

        assertEquals(true, login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void passwordDoesNotMeetComplexityRequirements() {
        Login login = new Login();

        assertEquals(false, login.checkPasswordComplexity("password"));
    }

    @Test
    public void cellPhoneNumberIsCorrectlyFormatted() {
        Login login = new Login();

        assertEquals(true, login.checkCellPhoneNumber("+27831234567"));
    }

    @Test
    public void cellPhoneNumberIsIncorrectlyFormatted() {
        Login login = new Login();

        assertEquals(false, login.checkCellPhoneNumber("0831234567"));
    }

    @Test
    public void successfulLoginReturnsTheCorrectMessage() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27831234567");
        boolean loginResult = login.loginUser("kyl_1", "Ch&&sec@ke99!");

        assertEquals("Welcome Kyle ,Smith it is great to see you again.", login.returnLoginStatus(loginResult));
    }

    @Test
    public void failedLoginReturnsTheCorrectMessage() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27831234567");
        boolean loginResult = login.loginUser("kyl_1", "wrongPassword");

        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(loginResult));
    }
}
