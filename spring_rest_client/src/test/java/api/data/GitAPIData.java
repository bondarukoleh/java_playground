package api.data;

public class GitAPIData {
    private final String mainUrl = "https://api.github.com/";

    public String getMainUrl() {
        return mainUrl;
    }

    public String getUsersUrl() {
        return mainUrl + "users/" ;
    }
}

