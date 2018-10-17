package com.zhong.web.dao;

import com.zhong.web.modle.FileInfo;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author 张中俊
 **/
public class FileInfoDaoTest {
    @Test
    public void getAllFilesTest() {
        ArrayList<FileInfo> files = FileInfoDao.getAllFiles(1);
        for (FileInfo fi : files) {
            System.out.println(fi);
        }
    }
}
