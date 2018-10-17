package com.zhong.core.utils;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 抽取文件中的内容
 * <br>该类用于建立look-up表以说明关键词和文件名的关系
 * <br>使用了Lucene第三方开源库来进行分词
 * 支持.txt .html .pdf .ppt .xls .doc
 * <br>
 * 如果是 .gif .jpeg .wmv .mpeg .mp4的文件，则将它们的文件名作为输入
 *
 * @author zhang
 */
public class ReadFileUtils implements Serializable {


    static int counter = 0;

    /**
     * 多线程抽取一堆文件中的内容，注意没有返回值。
     * <p>
     * <br>
     * 要想获得返回值，需要使用该类下的getL1() getL2()
     *
     * @param listOfFile 要分析的文件
     * @throws InterruptedException 异常
     * @throws ExecutionException   异常
     * @throws IOException          异常
     */
    public static DB extractTextPar(List<File> listOfFile)
            throws InterruptedException, ExecutionException, IOException {

        // 线程数目 有几个文件就开几个线程，（最多不超过处理器数目）
        // 我的电脑是4核心的
        int threads = 0;
        if (Runtime.getRuntime().availableProcessors() > listOfFile.size()) {
            threads = listOfFile.size();
        } else {
            threads = Runtime.getRuntime().availableProcessors();
        }

        //线程池
        ExecutorService service = Executors.newFixedThreadPool(threads);

        // 一个数组的list
        // 以4个线程为例，list中的每一个item都是一个数组
        // 以第一个item为例，第一个item里面就是第一个线程中要处理的文件
        ArrayList<List<File>> inputs = new ArrayList<List<File>>();

        System.out.println("Number of Threads " + threads);

        // 收集每个thread中要处理的文件
        for (int i = 0; i < threads; i++) {
            ArrayList<File> tmp = new ArrayList<>();
            // 最后一个线程 把所有剩下的文件都收集起来
            if (i == threads - 1) {
                for (int j = 0; j < listOfFile.size() / threads + listOfFile.size() % threads; j++) {
                    tmp.add(listOfFile.get((listOfFile.size() / threads) * i + j));
                }
            } else {// 其他线程
                tmp = new ArrayList<>();
                for (int j = 0; j < listOfFile.size() / threads; j++) {

                    tmp.add(listOfFile.get((listOfFile.size() / threads) * i + j));
                }
            }
            inputs.add(i, tmp);
        }

        //开始多线程处理
        List<Future<DB>> futures = new ArrayList<Future<DB>>();
        for (final List<File> input : inputs) {
            Callable<DB> callable = new Callable<DB>() {
                public DB call() throws Exception {
                    DB output = extractOneDoc(input);

                    return output;
                }
            };
            futures.add(service.submit(callable));
        }

        //停止多线程
        service.shutdown();

        /**
         * 一个静态变量，是keyword-filesName的映射
         */
        Multimap<String, String> lp1 = ArrayListMultimap.create();
        /**
         * 一个静态变量，是filesName-keyword的映射
         */
        Multimap<String, String> lp2 = ArrayListMultimap.create();
        //开始把所有线程得到的结果放在一起
        for (Future<DB> future : futures) {
            Set<String> keywordSet1 = future.get().getLookup1().keySet();
            Set<String> keywordSet2 = future.get().getLookup2().keySet();

            for (String key : keywordSet1) {
                lp1.putAll(key, future.get().getLookup1().get(key));
            }
            for (String key : keywordSet2) {
                lp2.putAll(key, future.get().getLookup2().get(key));
            }
        }

        return new DB(lp1, lp2);
    }

    /**
     * 抽取一个线程中的 所有文件中的内容
     *
     * @param listOfFile 要抽的文件
     * @return 一个extractOneDoc类
     * @throws FileNotFoundException 异常
     */
    public static DB extractOneDoc(List<File> listOfFile) throws FileNotFoundException {

        Multimap<String, String> lookup1 = ArrayListMultimap.create();
        Multimap<String, String> lookup2 = ArrayListMultimap.create();

        //处理每一个文件
        for (File file : listOfFile) {
            //提示用户当前的处理的进度
            //思路就是求出在每一个百分点出处理的文件数
            for (int j = 0; j < 100; j++) {
                if (counter == (int) ((j + 1) * listOfFile.size() / 100)) {
                    System.out.println("Number of files read equals " + j + " %");
                    break;
                }
            }
            //以行的形式存储文字
            List<String> lines = new ArrayList<String>();
            //计数器 用来记录现在处理过多少个文件
            counter++;
            FileInputStream fis = new FileInputStream(file);

            // 处理docx文件
            if (file.getName().endsWith(".docx")) {
                XWPFDocument doc;
                try {
                    doc = new XWPFDocument(fis);
                    XWPFWordExtractor ex = new XWPFWordExtractor(doc);
                    lines.add(ex.getText());
                } catch (IOException e) {
                    System.out.println("File not read: " + file.getName());
                }
            }

            // 处理pptx文件
            else if (file.getName().endsWith(".pptx")) {
                OPCPackage ppt;
                try {
                    ppt = OPCPackage.open(fis);
                    XSLFPowerPointExtractor xw = new XSLFPowerPointExtractor(ppt);
                    lines.add(xw.getText());
                } catch (XmlException e) {
                    System.out.println("File not read: " + file.getName());
                } catch (IOException e) {
                    System.out.println("File not read: " + file.getName());
                } catch (OpenXML4JException e) {
                    System.out.println("File not read: " + file.getName());
                }

            }

            // 处理xlsx文件
            else if (file.getName().endsWith(".xlsx")) {
                OPCPackage xls;
                try {
                    xls = OPCPackage.open(fis);
                    XSSFExcelExtractor xe = new XSSFExcelExtractor(xls);
                    lines.add(xe.getText());
                } catch (InvalidFormatException e) {
                    System.out.println("File not read: " + file.getName());
                } catch (IOException e) {
                    System.out.println("File not read: " + file.getName());
                } catch (XmlException e) {
                    System.out.println("File not read: " + file.getName());
                } catch (OpenXML4JException e) {
                    System.out.println("File not read: " + file.getName());
                }
            }

            // 处理doc文件
            else if (file.getName().endsWith(".doc")) {
                NPOIFSFileSystem fs;
                try {
                    fs = new NPOIFSFileSystem(file);
                    WordExtractor extractor = new WordExtractor(fs.getRoot());
                    for (String rawText : extractor.getParagraphText()) {
                        lines.add(extractor.stripFields(rawText));
                    }
                } catch (IOException e) {
                    System.out.println("File not read: " + file.getName());
                }

            }

            // 处理pdf文件
            else if (file.getName().endsWith(".pdf")) {
                PDFParser parser;
                COSDocument cd = null;
                try {
                    parser = new PDFParser(fis);
                    parser.parse();
                    cd = parser.getDocument();
                    PDFTextStripper stripper = new PDFTextStripper();
                    lines.add(stripper.getText(new PDDocument(cd)));
                } catch (IOException e) {
                    System.out.println("File not read: " + file.getName());
                } finally {
                    try {
                        cd.close();
                    } catch (IOException e) {
                        System.out.println("File not read: " + file.getName());
                    }
                }

            }

            // 处理媒体文件
            else if (file.getName().endsWith(".gif") || file.getName().endsWith(".jpeg")
                    || file.getName().endsWith(".wmv") || file.getName().endsWith(".mpeg")
                    || file.getName().endsWith(".mp4")) {

                lines.add(file.getName());

            }

            // 处理txt文件
            else {
                try {

                    lines = Files.readLines(file, Charsets.UTF_8);
                } catch (IOException e) {
                    System.out.println("File not read: " + file.getName());
                } finally {
                    try {
                        fis.close();
                    } catch (IOException ioex) {
                        System.out.println("File not read: " + file.getName());
                    }
                }
            }

            // 开始抽取关键字
            int temporaryCounter = 0;

            // Filter threshold
            int counterDoc = 0;
            for (int i = 0; i < lines.size(); i++) {

                CharArraySet noise = EnglishAnalyzer.getDefaultStopSet();

                //在这里使用了一个标准的分词器，没有过滤掉任何单词
                Analyzer analyzer = new StandardAnalyzer(noise);
                List<String> token = Tokenizer.tokenizeString(analyzer, lines.get(i));
                temporaryCounter = temporaryCounter + token.size();
                for (int j = 0; j < token.size(); j++) {
                    //避免重复记录关键词 文件名-关键词的对应关系
                    if (!lookup2.get(file.getName()).contains(token.get(j))) {
                        lookup2.put(file.getName(), token.get(j));
                    }

                    //避免重复记录文件 关键词-文件名的对应关系
                    if (!lookup1.get(token.get(j)).contains(file.getName())) {
                        lookup1.put(token.get(j), file.getName());
                    }
                }

            }
        }
        return new DB(lookup1, lookup2);

    }
}
