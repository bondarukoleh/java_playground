package suiteTesting;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {
    @Test
    public void testUser() {
        UserPage loginPage = new UserPage();
        Assert.assertEquals("a", "a");
    }
}
