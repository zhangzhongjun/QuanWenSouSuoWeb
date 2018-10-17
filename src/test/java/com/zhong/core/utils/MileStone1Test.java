package com.zhong.core.utils;


import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author 张中俊
 **/
public class MileStone1Test {
    @Test
    public void t1() throws InterruptedException, ExecutionException, IOException {
        File dict = com.zhong.utils.MyUtils.getFile("upload", "");
        List<File> files = new ArrayList<>();
        MyUtils.listf(dict.getName(), files);
        long t1 = System.currentTimeMillis();
        DB db = ReadFileUtils.extractTextPar(files);
        long t2 = System.currentTimeMillis();
        System.out.println("多线程需要时间：" + (t2 - t1) + " ms");

        String dir = com.zhong.utils.MyUtils.getFile("BloomFilters", "").getAbsolutePath();
        BloomFilterWrapper.writeBF(db, dir);
    }

    @Test
    public void t2() throws IOException {
        File file = MyUtils.getFile("uploads", "入门.py");
        List<File> files = new ArrayList<>();
        files.add(file);
        long t1 = System.currentTimeMillis();
        DB db = ReadFileUtils.extractOneDoc(files);
        long t2 = System.currentTimeMillis();
        System.out.println("提取关键词需要时间：" + (t2 - t1) + " ms");

        String dir = MyUtils.getFile("blooms", "").getAbsolutePath();
        BloomFilterWrapper.writeBF(db, dir);
    }
}
