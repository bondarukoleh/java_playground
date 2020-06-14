package api.objects.git;

import models.GitGist;

public class GitGistsAPI extends BaseGitAPIObject {
    public GitGist[] getGists() {
        return restClient.get(gitAPIData.getGistsUrl(), GitGist[].class);
    }
}
