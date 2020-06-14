package tests;

import api.objects.git.GitGistsAPI;
import api.objects.git.GitUsersAPI;
import models.GitGist;
import models.GitUser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GistUserTest {

    private GitGistsAPI gitGistsAPI = new GitGistsAPI();
    private String userName = "bondarukoleh";

    @Test
    public void getUserGistsTest() {
        GitGist[] gists = gitGistsAPI.getGists();
        System.out.println(Arrays.toString(gists));
//        Assert.assertEquals(userName,  user.getLoginName());
    }
}
