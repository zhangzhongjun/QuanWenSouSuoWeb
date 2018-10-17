package com.zhong.utils;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 张中俊
 **/
public class MysqlHelperTest {
    @Test
    public void executeQueryTest() throws SQLException {
        String sql = "select * from users;";
        ResultSet rs = MysqlHelper.executeQuery(sql);
        System.out.println(rs);
        while (rs.next()) {
            System.out.print(rs.getInt("id") + " ");
            System.out.print(rs.getString("username") + " ");
            System.out.println(rs.getString("password"));
        }
        rs.close();
    }
}
