package models;

public class GitGist {
    private String id;
    private GitUser owner;

    public GitGist(String id, GitUser owner) {
        this.id = id;
        this.owner = owner;
    }
}
