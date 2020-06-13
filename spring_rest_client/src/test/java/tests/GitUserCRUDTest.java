package tests;

import api.objects.GitUsersAPI;
import models.GitUser;
import org.junit.Assert;
import org.junit.Test;

public class GitUserCRUDTest {

    private GitUsersAPI gitUsersAPI = new GitUsersAPI();
    private String userName = "bondarukoleh";

    @Test
    public void getGitUserTest() {
        GitUser user = gitUsersAPI.getUser(userName);
        Assert.assertEquals(userName,  user.getLoginName());
    }
}
