package com.zhong.core.utils;

import bloomfilter.mutable.BloomFilter;
import com.zhong.utils.MyUtils;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-04-12 20:09
 **/
public class BloomFilterWrapperTest {
    @Test
    public void writeBFTest() throws Exception {
        ArrayList<byte[]> t = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            t.add(("ind" + i).getBytes("utf-8"));
        }
        String path = com.zhong.utils.MyUtils.getFile("bf", "test.bf").getAbsolutePath();
        BloomFilterWrapper.writeBF(t, path);
    }

    @Test
    public void searchTest() throws UnsupportedEncodingException {
        File file = com.zhong.utils.MyUtils.getFile("BloomFilters", "");
        ArrayList<String> res = BloomFilterWrapper.search(file.getAbsolutePath(), "code");
        for (String s : res) {
            System.out.println(s);
        }
    }

    @Test
    public void getBFTest() throws UnsupportedEncodingException {
        String path = MyUtils.getFile("bf", "test.bf").getAbsolutePath();
        BloomFilter<byte[]> bf = BloomFilterWrapper.getBF(path);
        for (int i = 0; i < 120; i++) {
            byte[] t = ("ind" + i).getBytes("utf-8");
            System.out.println(i + " " + bf.mightContain(t));
        }
        bf.dispose();
    }
}
