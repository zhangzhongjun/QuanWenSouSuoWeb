package com.zhong.utils;

/**
 * @author 张中俊
 * @create 2018-04-03 8:30
 **/

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * MySql连接池
 */
public class MysqlConnPool {
    private static final MysqlConnPool instance = new MysqlConnPool();
    private static Properties pro;
    private static ComboPooledDataSource comboPooledDataSource;

    static {
        try {
            String dirPath = MysqlConnPool.class.getClassLoader().getResource("").toURI().getPath();

            File f = new File(dirPath, "config.properties");
            String path = f.getAbsolutePath();
            pro = MyUtils.getUpConfig(path);

            String url = null;
            String username;
            String password;
            url = pro.getProperty("mysql.connecturl");
            username = pro.getProperty("mysql.username");
            password = pro.getProperty("mysql.password");
            Class.forName("com.mysql.jdbc.Driver");
            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
            comboPooledDataSource.setJdbcUrl(url);
            comboPooledDataSource.setUser(username);
            comboPooledDataSource.setPassword(password);
            //下面是设置连接池的一配置
            comboPooledDataSource.setMaxPoolSize(20);
            comboPooledDataSource.setMinPoolSize(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得mysql连接
     *
     * @return 得到的mysql的连接
     */
    public synchronized static Connection getConnection() {
        Connection connection = null;
        try {
            connection = comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return connection;
        }
    }
}

