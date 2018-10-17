package com.zhong.core.utils;

import bloomfilter.CanGenerateHashFrom;
import bloomfilter.mutable.BloomFilter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 张中俊
 **/
public class BloomFilterWrapper {

    /**
     * 将一些元素写入到BloomFilter中，并将其写入到文件中
     *
     * @param elements 要写入的元素
     * @param path     文件的路径
     */
    public static void writeBF(List<byte[]> elements, String path) {
        //BF的长度 设置100的冗余
        long expectedElements = elements.size() + 100;
        //误判率
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);
        for (byte[] element : elements) {
            bf.add(element);
        }
        SerializationDemonstrator.serialize(bf, path);
        bf.dispose();
    }

    /**
     * 将数据库中的key-id信息写入到BloomFilter中
     *
     * @param db  key-id对的信息
     * @param dir 文件夹名称
     * @throws UnsupportedEncodingException 异常
     */
    public static void writeBF(DB db, String dir) throws UnsupportedEncodingException {
        for (String filename : db.getLookup2().keySet()) {
            String path = new File(dir, filename).getAbsolutePath();
            Collection<String> keywords = db.getLookup2().get(filename);
            List<byte[]> elements = new ArrayList<>();
            for (String keyword : keywords) {
                elements.add(keyword.getBytes("utf-8"));
            }
            writeBF(elements, path);
        }
    }

    /**
     * 搜索关键词
     *
     * @param path    布隆过滤器的路径，是一个文件夹。本函数将遍历该文件夹下的所有的文件。
     * @param keyword 要搜索的关键词
     * @return 包含keyword的布隆过滤器的文件名
     * @throws UnsupportedEncodingException 异常
     */
    public static ArrayList<String> search(String path, String keyword) throws UnsupportedEncodingException {
        ArrayList<String> res = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            BloomFilter bf = getBF(file.getAbsolutePath());
            if (bf.mightContain(keyword.getBytes("utf-8"))) {
                res.add(file.getName());
            }
        }
        return res;
    }


    /**
     * 获得文件中存储的BF
     *
     * @param path 文件路径
     * @return 文件存储的BF
     */
    public static BloomFilter<byte[]> getBF(String path) {
        return SerializationDemonstrator.deserialize(path);
    }
}
