package com.zhong.core.utils;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author 张中俊
 **/
public class ReadFileUtilsTest {
    @Test
    public void extractOneDocTest() throws FileNotFoundException {
        File dict = com.zhong.utils.MyUtils.getFile("uploads", "");
        List<File> files = new ArrayList<>();
        MyUtils.listf(dict.getName(), files);

        long t1 = System.currentTimeMillis();
        DB db = ReadFileUtils.extractOneDoc(files);
        long t2 = System.currentTimeMillis();
        System.out.println(db);
        System.out.println("单线程需要时间：" + (t2 - t1) + " ms");
    }

    @Test
    public void extractTextParTest() throws IOException, InterruptedException, ExecutionException {
        File dict = com.zhong.utils.MyUtils.getFile("uploads", "");
        List<File> files = new ArrayList<>();
        MyUtils.listf(dict.getName(), files);
        long t1 = System.currentTimeMillis();
        DB db = ReadFileUtils.extractTextPar(files);
        long t2 = System.currentTimeMillis();

        System.out.println(db);
        System.out.println("多线程需要时间：" + (t2 - t1) + " ms");
    }

    @Test
    public void extractOnlyOneFileDocTest() throws Exception {
        File file = MyUtils.getFile("uploads", "入门.py");
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        long t1 = System.currentTimeMillis();
        DB db = ReadFileUtils.extractOneDoc(files);
        long t2 = System.currentTimeMillis();

        System.out.println(db);
        System.out.println("提取一个文件需要时间：" + (t2 - t1) + " ms");
    }
}
