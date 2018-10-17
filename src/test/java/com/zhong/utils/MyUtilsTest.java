package com.zhong.utils;

import org.junit.Test;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

/**
 * @author 张中俊
 **/
public class MyUtilsTest {
    @Test
    public void getFileTest() {
        File f = MyUtils.getFile("src/main/java/com", "zhong");
        System.out.println(f.getAbsolutePath());
    }

    @Test
    public void getUpConfigTest() {
        File f = MyUtils.getFile("src/main/webapp/WEB-INF", "config.properties");
        String path = f.getAbsolutePath();
        Properties properties = MyUtils.getUpConfig(path);

        System.out.println(properties.getProperty("hello"));
        System.out.println(properties.getProperty("txt"));
        System.out.println(properties.getProperty("hello.world"));
    }

    @Test
    public void UUIDTest() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid.length());
    }
}
