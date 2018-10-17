package com.zhong.core.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

/**
 * @author 张中俊
 * <p>
 * 数据库中的数据。即关键词和文件名的信息
 */
public class DB {
    /**
     * 关键词到文件名的映射
     */
    Multimap<String, String> lookup1 = ArrayListMultimap.create();
    /**
     * 文件名到关键词的映射
     */
    Multimap<String, String> lookup2 = ArrayListMultimap.create();

    public DB(Multimap<String, String> lookup1, Multimap<String, String> lookup2) {
        this.lookup1 = lookup1;
        this.lookup2 = lookup2;
    }

    public Multimap<String, String> getLookup1() {
        return lookup1;
    }

    public void setLookup1(Multimap<String, String> lookup1) {

        this.lookup1 = lookup1;
    }

    public Multimap<String, String> getLookup2() {
        return lookup2;
    }

    public void setLookup2(Multimap<String, String> lookup2) {
        this.lookup2 = lookup2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========您正在查看DB的内容========");
        sb.append(System.lineSeparator());
        for (String key : lookup1.keySet()) {
            sb.append("关键词 " + key + ": ");
            Collection<String> filenames = lookup1.get(key);
            for (String filename : filenames) {
                sb.append(filename + " ");
            }
            sb.append(System.lineSeparator());
        }

        for (String key : lookup2.keySet()) {
            sb.append("文件名 " + key + "包含的关键词: ");
            Collection<String> keywords = lookup2.get(key);
            for (String keyword : keywords) {
                sb.append(keyword + " ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("========================================");
        return sb.toString();
    }
}
