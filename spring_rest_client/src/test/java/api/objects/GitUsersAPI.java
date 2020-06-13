package api.objects;

import api.RestClient;
import api.data.GitAPIData;
import models.GitUser;

public class GitUsersAPI {
    private GitAPIData gitAPIData = new GitAPIData();
    private RestClient restClient = new RestClient();

    public GitUser getUser(String userLogin) {
        return restClient.get(gitAPIData.getUsersUrl() + userLogin, GitUser.class);
    }
}
