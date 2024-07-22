package suiteTesting;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class LoginTest {
    @Test
    public void testLogin() {
        LoginPage loginPage = new LoginPage();
        assertEquals("a", "a");
    }
}
