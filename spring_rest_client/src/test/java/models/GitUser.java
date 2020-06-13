package models;

import java.io.Serializable;

public class GitUser implements Serializable {
    public String login;
    private Integer id;
    private String avatar_url;
    private String followers_url;
    private String gists_url;
    private String type;
    private Boolean site_admin;
    private String name;
    private String bio;
    private Integer public_repos;

    public GitUser() {}

    public GitUser(String login,
                   Integer id,
                   String avatar_url,
                   String followers_url,
                   String gists_url,
                   String type,
                   Boolean site_admin,
                   String name,
                   String bio,
                   Integer public_repos) {
        this.login = login;
        this.id = id;
        this.avatar_url = avatar_url;
        this.followers_url = followers_url;
        this.gists_url = gists_url;
        this.type = type;
        this.site_admin = site_admin;
        this.name = name;
        this.bio = bio;
        this.public_repos = public_repos;
    }

    public String getLoginName() {
        return login;
    }

    public void setLoginName(String loginName) {
        this.login = loginName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public String getGists_url() {
        return gists_url;
    }

    public void setGists_url(String gists_url) {
        this.gists_url = gists_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSite_admin() {
        return site_admin;
    }

    public void setSite_admin(Boolean site_admin) {
        this.site_admin = site_admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(Integer public_repos) {
        this.public_repos = public_repos;
    }

    @Override
    public String toString() {
        return "User{" +
                "loginName='" + login + '\'' +
                ", id=" + id +
                ", avatar_url='" + avatar_url + '\'' +
                ", followers_url='" + followers_url + '\'' +
                ", gists_url='" + gists_url + '\'' +
                ", type='" + type + '\'' +
                ", site_admin=" + site_admin +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", public_repos=" + public_repos +
                '}';
    }
}
