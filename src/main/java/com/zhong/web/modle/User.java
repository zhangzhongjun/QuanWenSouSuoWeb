package com.zhong.web.modle;

/**
 * @author 张中俊
 **/
public class User {
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {

        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("====正在查看User的信息======");
        sb.append(System.lineSeparator());
        sb.append("id: " + id);
        sb.append(System.lineSeparator());
        sb.append("username: " + username);
        sb.append(System.lineSeparator());
        sb.append("password: " + password);
        sb.append(System.lineSeparator());
        sb.append("=================================");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
