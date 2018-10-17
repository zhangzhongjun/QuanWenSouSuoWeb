package com.zhong.web.dao;

import com.zhong.utils.MysqlConnPool;
import com.zhong.web.modle.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User类的Dao
 *
 * @author 张中俊
 **/
public class UserDao {
    /**
     * 向数据库中插入数据
     *
     * @param user 要插入的数据
     */
    public static void addUser(User user) {
        Connection conn = MysqlConnPool.getConnection();
        PreparedStatement pres = null;
        String sql1 = "insert into users(username,password) values(?,?)";
        try {
            pres = conn.prepareStatement(sql1);
            pres.setString(1, user.getUsername());
            pres.setString(2, user.getPassword());
            pres.execute();

            if (pres != null)
                pres.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接时候出错");
            }
        }
    }

    /**
     * 是否是合法用户
     *
     * @param user 要查询的用户
     * @return 如果存在，返回ID；否则返回-1
     */
    public static User isValid(User user) {
        Connection conn = MysqlConnPool.getConnection();
        PreparedStatement pres = null;
        User task = null;
        String sql_XSets = "select * from users WHERE username=(?) and password=(?) limit 1";
        try {
            pres = conn.prepareStatement(sql_XSets);
            pres.setString(1, user.getUsername());
            pres.setString(2, user.getPassword());
            ResultSet res = pres.executeQuery();
            if (res.next()) {
                int id = res.getInt(1);
                return new User(id, user.getUsername(), user.getPassword());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pres != null)
                    pres.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接时候出错");
            }
        }
        return null;
    }


    /**
     * 是否已经被注册
     *
     * @param user 要查询的用户
     * @return 是否已经被注册
     */
    public static boolean hasBeenRegisted(User user) {
        Connection conn = MysqlConnPool.getConnection();
        PreparedStatement pres = null;
        User task = null;
        String sql_XSets = "select count(*) from users WHERE username=(?) limit 1";
        try {
            pres = conn.prepareStatement(sql_XSets);
            pres.setString(1, user.getUsername());
            ResultSet res = pres.executeQuery();
            res.next();
            int count = res.getInt(1);

            if (pres != null)
                pres.close();

            if (count == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("关闭连接时候出错");
            }
        }
        return false;
    }
}
