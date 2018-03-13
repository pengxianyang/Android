package com.example.administrator.lab9;

/**
 * Created by Administrator on 2017/12/21.
 */

public class Github {
    public String login;
    public String blog;
    public int id;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Github(String login, String blog, int id) {
        this.login = login;
        this.blog = blog;
        this.id = id;
    }
}
