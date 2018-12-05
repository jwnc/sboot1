package jijin;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import com.wnc.basic.BasicNumberUtil;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import translate.util.JsoupHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 零费率基金爬取
 * @author nengcai.wang
 * @version: 1.0
 * @since 2018/12/5 18:29
*/
public class Zero {
    private  ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    private static class Couple implements  Comparable<Couple>{
        private double num;
        private String str;

        public Couple(double num, String str) {
            this.num = num;
            this.str = str;
        }

        public double getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        @Override
        public int compareTo(@NotNull Couple o) {
            if(o == null){
                return 1;
            }
            if(o.num == this.num){
                return 0;
            }
            return (o.num - this.num) > 0 ? 1 : -1;
        }
    }
    @Test
    public void a() throws InterruptedException {
        System.out.println(this.getClass().getResource("/").getPath());
        final List<Couple> couples = new ArrayList<Couple>();
        final String lock = "lock";
        Document parse = Jsoup.parse(StringUtils.join(FileOp.readFrom(this.getClass().getResource("/").getPath()+"jijin.txt"), ""));
        Elements trs = parse.select("#dbtable > tbody > tr");
        for(Element e : trs){
//            System.out.println(e.text());
            Elements td = e.select("td");
            String text = td.get(18).text();
            final String url = td.get(18).select("a").attr("href");
            if(text.equals("0.00%")){
//                System.out.println(url);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Document documentResult = JsoupHelper.getDocumentResult(url);
                            Elements boxs = documentResult.select("div.box");
                            String moneyTotal = PatternUtil.getFirstPatternGroup(documentResult.select(".bs_gl").toString(), "([\\d.]+亿元)");
                            String foundDay = PatternUtil.getFirstPatternGroup(documentResult.select(".bs_gl").toString(), "(成立日期：[0-9\\-]+)");
                            for(Element box : boxs){
                                if(box.toString().contains("赎回费率")){
                                    Elements trs = box.select("table.w650.comm.jjfl tbody tr");
                                    String s = documentResult.title().replaceAll("基金费率 _ 基金档案 _ 天天基金网", "")+"\t"+moneyTotal+"\t"+url+"\n";
                                    boolean out = false;
                                    synchronized (lock) {
                                        for (Element tr : trs) {
                                            s += tr.select("td:eq(1)").text() + "\t" + tr.select("td:eq(2)").text() +"\n";
                                            if(tr.select("td:eq(1)").text().startsWith("大于等于7天") && tr.select("td:eq(2)").text().equals("0.00%")){
                                                out = true;
                                            }
                                        }
                                        if(out){
                                            couples.add(new Couple(BasicNumberUtil.getDouble(moneyTotal.replaceAll("([\\d.]+).+", "$1")), s));
                                        }
                                    }
                                    break;
                                }
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });
            }
        }
        while(executorService.getQueue().size() > 0 || executorService.getActiveCount() > 0) {
            Thread.sleep(1111L);
        }
        Collections.sort(couples);
        for(Couple c : couples){
            System.err.println(c.getStr());
        }
    }
}
