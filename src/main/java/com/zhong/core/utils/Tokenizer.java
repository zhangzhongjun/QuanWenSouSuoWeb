package com.zhong.core.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于Lucene的Tokenizer，即分词器
 * <br>可以设置一些过滤词(stop word)
 * <br>分词器的作用是：给定一个字符串，返回一个分词得到的数组
 *
 * @author 张中俊
 */
public final class Tokenizer {

    private Tokenizer() {
    }

    public static List<String> tokenizeString(Analyzer analyzer, String string) {
        List<String> result = new ArrayList<String>();
        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(string));
            stream.reset();
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
