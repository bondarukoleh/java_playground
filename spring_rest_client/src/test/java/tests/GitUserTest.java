package tests;

import api.objects.git.GitUsersAPI;
import models.GitUser;
import org.junit.Assert;
import org.junit.Test;

public class GitUserTest {

    private GitUsersAPI gitUsersAPI = new GitUsersAPI();
    private String userName = "bondarukoleh";

    @Test
    public void getUserTest() {
        GitUser user = gitUsersAPI.getUser(userName);
        Assert.assertEquals(userName,  user.getLoginName());
    }
}
