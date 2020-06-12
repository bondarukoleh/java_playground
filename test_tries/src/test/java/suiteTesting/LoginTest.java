package suiteTesting;

import org.junit.Assert;
import org.junit.Test;

public class LoginTest {
    @Test
    public void testLogin() {
        LoginPage loginPage = new LoginPage();
        Assert.assertEquals("a", "a");
    }
}
