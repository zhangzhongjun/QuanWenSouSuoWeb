package com.zhong.core.utils;

import java.io.*;
import java.util.*;

public class MyUtils {

    /**
     * AES加密时候使用的初始向量
     */
    private static final byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 获得文件夹下的所有文件，即获得directoryName文件夹下的所有文件到files
     *
     * @param directoryName 要获取的文件夹
     * @param files         要将文件保存在files中
     */
    public static void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
            }
        }
    }

    // 获得resources文件夹下的文件
    public static File getFile(String dirname, String fileName) {
        String path = System.getProperty("user.dir");
        path = path + File.separator + dirname;
        File file = new File(path, fileName);
        return file;
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

    /**
     * 获得一个测试数据集<br>
     * keyword1: keyword1_filename1,keyword1_filename2,keyword1_filename3<br>
     * keyword2: keyword2_filename1,keyword2_filename2,keyword2_filename3<br>
     * keyword3: keyword3_filename1,keyword3_filename2,keyword3_filename3
     *
     * @return
     */
    public static Map<String, Collection<String>> getAToyDB() {
        Map<String, Collection<String>> kf = new HashMap<String, Collection<String>>();
        List<String> filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        filenames.add("ind3");
        kf.put("keyword1", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        kf.put("keyword2", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        kf.put("keyword3", filenames);

        return kf;
    }

    public static Map<String, Collection<String>> getAToyDB2() {
        Map<String, Collection<String>> kf = new HashMap<String, Collection<String>>();
        List<String> filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        filenames.add("ind3");
        kf.put("keyword1", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        kf.put("keyword2", filenames);

        filenames = new ArrayList<String>();
        filenames.add("ind1");
        filenames.add("ind2");
        kf.put("keyword3", filenames);

        return kf;
    }

    public static Map<String, Collection<String>> getAToyDB3() {
        Map<String, Collection<String>> kf = new HashMap<String, Collection<String>>();
        List<String> filenames = new ArrayList<String>();
        filenames.add("b80aee33-0aa9-4909-9e69-7f14a1fa0be5.txt");
        filenames.add("bac0ade3-d75a-46b1-a649-4539818c696b.txt");
        filenames.add("368ff846-e651-47cd-9091-e40b6a4e786e.txt");
        kf.put("zalk", filenames);

        filenames = new ArrayList<String>();
        filenames.add("4915af53-89f8-45c3-93b9-560202171899.txt");
        filenames.add("b0f910a5-e242-4649-809b-cb6a89fff76f.txt");
        filenames.add("31e3a41d-ddd9-4966-bffa-070551b66d63.txt");
        filenames.add("c120b771-cc8f-4f22-b8c8-1bfcd83dd890.txt");
        kf.put("zamarippa", filenames);

        return kf;
    }

}
