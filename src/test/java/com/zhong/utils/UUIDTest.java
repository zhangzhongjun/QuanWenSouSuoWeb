package com.zhong.utils;

import org.junit.Test;

import java.util.UUID;

/**
 * @author 张中俊
 **/
public class UUIDTest {
    @Test
    public void uuidTest() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid.length());
    }
}
