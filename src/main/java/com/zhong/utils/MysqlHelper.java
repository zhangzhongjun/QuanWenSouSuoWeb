package com.zhong.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mysql的wrapper
 *
 * @author 张中俊
 **/
public class MysqlHelper {
    /**
     * 执行update、insert、delete等语句，返回值为受影响的行数
     *
     * @param sql sql语句
     * @return 受到影响的行数
     */
    public static int executeUpdate(String sql) {
        Connection conn = MysqlConnPool.getConnection();
        int resCount = 0;
        if (sql == null || sql.isEmpty()) {
            System.out.println("sql语句不能为空");
            return resCount;
        }
        PreparedStatement ps = null;
        System.out.println("sql--> " + sql);
        try {
            ps = conn.prepareStatement(sql);
            resCount = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resCount;
    }

    /**
     * 执行能够返回结果集的查询语句
     *
     * @param sql 要执行的sql语句
     * @return 查询结果集
     */
    public static ResultSet executeQuery(String sql) {
        Connection connection = MysqlConnPool.getConnection();
        if (sql.isEmpty()) {
            System.out.println("sql语句不为空");
            return null;
        }
        ResultSet rs = null;
        PreparedStatement ps = null;
        System.out.println("sql--> " + sql);
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                //这里不能关闭，如果关闭的话调用者将不能使用ResultSet
                //ps.close();
                //connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
}
