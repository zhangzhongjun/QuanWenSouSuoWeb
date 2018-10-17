package com.zhong.utils;

import java.io.*;
import java.util.Properties;

public class MyUtils {


    /**
     * 获得工程下的某个文件或文件夹
     *
     * @param dirname  文件夹名称
     * @param fileName 文件名
     * @return 该文件或目录
     */
    public static File getFile(String dirname, String fileName) {
        String path = System.getProperty("user.dir");
        path = path + File.separator + dirname;
        File file = new File(path, fileName);
        return file;
    }

    /**
     * 获得配置文件
     *
     * @param path 配置文件的路径
     * @return 配置文件对应的Properities对象
     */
    public static Properties getUpConfig(String path) {
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream(path);
            prop.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }


    /**
     * 将对象转化为数组
     *
     * @param o 待转化的对象
     * @return byte[]数组
     */
    public static byte[] msg2Byte(Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 将byte[]数组转化为对象
     *
     * @param bytes 待转化的数组
     * @return 转化为的对象
     */
    public static Object byte2Msg(byte[] bytes) {
        ByteArrayInputStream bais;
        ObjectInputStream in = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);

            return in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
