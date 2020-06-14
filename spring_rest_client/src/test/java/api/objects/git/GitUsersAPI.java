package api.objects.git;

import models.GitUser;

public class GitUsersAPI extends BaseGitAPIObject {
    public GitUser getUser(String userLogin) {
        return restClient.get(gitAPIData.getUsersUrl() + userLogin, GitUser.class);
    }
}
