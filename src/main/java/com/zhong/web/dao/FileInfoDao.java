package com.zhong.web.dao;

import com.zhong.utils.MysqlConnPool;
import com.zhong.web.modle.FileInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 张中俊
 **/
public class FileInfoDao {

    public static ArrayList<FileInfo> getAllFiles(int userid) {
        Connection conn = MysqlConnPool.getConnection();
        PreparedStatement pres = null;
        String sql = "select * from file_info WHERE userid=(?)";
        try {
            pres = conn.prepareStatement(sql);
            pres.setInt(1, userid);
            ResultSet res = pres.executeQuery();

            ArrayList<FileInfo> files = new ArrayList<>();
            while (res.next()) {
                String id = res.getString(1);
                String filename = res.getString(2);
                String filepath = res.getString(3);
                Timestamp timestamp = res.getTimestamp(4);
                userid = res.getInt(5);
                boolean isdeleted = res.getBoolean(6);
                FileInfo fi = new FileInfo(id, filename, filepath, new Date(timestamp.getTime()), userid, isdeleted);
                files.add(fi);
            }

            return files;
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
}
