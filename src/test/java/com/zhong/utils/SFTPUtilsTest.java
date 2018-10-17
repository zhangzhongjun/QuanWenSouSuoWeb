package com.zhong.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 张中俊
 **/
public class SFTPUtilsTest {

    @Test
    public void uploadFileTest() throws Exception {
        SFTPUtils.upload("shared", "C://Users//zhang//Documents//GitHub//MyDoc//新建文本文档.txt");
        SFTPUtils.disconnect();
    }

    @Test
    public void downloadFileTest() throws Exception {
        SFTPUtils.download("shared", "新建文本文档.txt", "f://下载到的.txt");
        SFTPUtils.disconnect();
    }

    @Test
    public void uploadFileTest2() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());
        SFTPUtils.upload("shared" + "/" + s.replace("-", "/"), "C://Users//zhang//Documents//GitHub//MyDoc//新建文本文档.txt");
        SFTPUtils.disconnect();
    }
}
