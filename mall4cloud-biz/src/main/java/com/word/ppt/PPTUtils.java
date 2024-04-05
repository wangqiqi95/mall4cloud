package com.word.ppt;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.word.pdf.PagesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Date 2020年7月31日, 0031 10:41
 * @Created by eury
 */
@Slf4j
public class PPTUtils {

    private final static org.slf4j.Logger logger= LoggerFactory.getLogger(PPTUtils.class);

    public static void main(String[] strings){
        String path="/Users/guomengqi/Desktop/ksk_crm/素材解析/gomall.pptx";
        String dirPath="/Users/guomengqi/Desktop/ksk_crm/素材解析/imgs";
        File pptFile=new File(path);
        List<String> list=new ArrayList<>();
//        doPPTtoImage(pptFile,5,list);
        ppt2Imgs(pptFile,dirPath,5,list);
        System.out.println(JSON.toJSONString(list));
    }


    /**
     * pdf转图片【多线程处理】
     * @param pptFile pdf文件路径
     * @param dirPath 转出图片存储路径，传空则默认路径
     */
    public static void ppt2Imgs(File pptFile, String dirPath,int times,List<String> list) {
        InputStream is = null;
        SlideShow slideShow = null;
        try {
            log.info("开始执行ppt转图片-多线程处理");
            long start = new Date().getTime();
            // 延迟解析比率
            ZipSecureFile.setMinInflateRatio(-1.0d);//拉链炸弹检测 设置接受所有可能的比率.
            is = new FileInputStream(pptFile);
            String prefix = FileUtil.getSuffix(pptFile.getName());
            if (prefix.equalsIgnoreCase("ppt")) {
                slideShow = new HSLFSlideShow(is);
            } else if (prefix.equalsIgnoreCase("pptx")) {
                slideShow = new XMLSlideShow(is);
            }
            logger.info("doPPTtoImage---> slideShow:{}",slideShow);
            if (slideShow != null) {
                is.close();
                // 获取大小
                Dimension pgsize = slideShow.getPageSize();
                List<Slide> slides=(List<Slide>) slideShow.getSlides();
                int pages = slides.size();
                List<String> imgList = PagesUtils.getPaths(dirPath,pages);
                // 创建一个初始值为5的倒数计数器
                int downLatch=3;
                if(pages<downLatch){
                    downLatch=pages;
                }
                CountDownLatch countDownLatch = new CountDownLatch(downLatch);
                for(int i = 0; i < 3; i++) {
                    int begin = i*PagesUtils.getPageCount(pages);
                    int end = (i+1)*PagesUtils.getPageCount(pages);
                    if (begin >= end) {
                        end = begin;
                    }
                    if (i == 2) {
                        Thread thread = new ConvertThradPPTImage(countDownLatch, slides, imgList,pgsize,times,begin,pages);
                        thread.start();
                    } else {
                        Thread thread = new ConvertThradPPTImage(countDownLatch, slides, imgList,pgsize,times,begin,end);
                        thread.start();
                    }
                }
                // 阻塞当前线程，直到倒数计数器倒数到0
                countDownLatch.await();
                list.addAll(imgList);
                long end = new Date().getTime();
                logger.info("PPT转换成图片 成功，行数:{}",list.size());
                log.info("ppt-to-images，耗时：{}s", ((end - start) / 1000.0) );
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (slideShow != null) {
                    slideShow.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pdf转图片【单线程处理】
     * @param pptFile
     * @param times
     * @param list
     */
    public static void doPPTtoImage(File pptFile,int times,List<String> list){
        InputStream is = null;
        SlideShow slideShow = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            // 延迟解析比率
            log.info("开始执行ppt转图片-单线程处理");
            long start = new Date().getTime();
            ZipSecureFile.setMinInflateRatio(-1.0d);//拉链炸弹检测 设置接受所有可能的比率.
            is = new FileInputStream(pptFile);
            String prefix = FileUtil.getSuffix(pptFile.getName());
            if (prefix.equalsIgnoreCase("ppt")) {
                slideShow = new HSLFSlideShow(is);
            } else if (prefix.equalsIgnoreCase("pptx")) {
                slideShow = new XMLSlideShow(is);
            }
            logger.info("doPPTtoImage---> slideShow:{}",slideShow);
            if (slideShow != null) {
                is.close();
                // 获取大小
                Dimension pgsize = slideShow.getPageSize();
                // 获取幻灯片
                int page=0;
                for (Slide slide : (List<Slide>) slideShow.getSlides()) {
                    page++;
                    List shapes = slide.getShapes();
                    if (shapes != null) {
                        for (int i = 0; i < shapes.size(); i++) {
                            Shape shape = (Shape) shapes.get(i);
                            //处理文字
                            if (shape instanceof XSLFTextShape) {
                                XSLFTextShape sh = (XSLFTextShape) shape;
                                List<XSLFTextParagraph> textParagraphs = sh.getTextParagraphs();
                                for (XSLFTextParagraph xslfTextParagraph : textParagraphs) {
                                    List<XSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                                    for (XSLFTextRun xslfTextRun : textRuns) {
                                        xslfTextRun.setFontFamily("宋体");
                                        xslfTextRun.setFontSize(xslfTextRun.getFontSize()-3);
                                    }
                                }
                            }
                            if (shape instanceof HSLFTextShape) {
                                HSLFTextShape sh = (HSLFTextShape) shape;
                                List<HSLFTextParagraph> textParagraphs = sh.getTextParagraphs();
                                for (HSLFTextParagraph xslfTextParagraph : textParagraphs) {
                                    List<HSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                                    for (HSLFTextRun xslfTextRun : textRuns) {
                                        xslfTextRun.setFontSize(xslfTextRun.getFontSize()-2);
                                        xslfTextRun.setFontFamily("宋体");
                                    }
                                }
                            }
                            //处理表格
                        }
                    }

                    //根据幻灯片大小生成图片
                    BufferedImage img = new BufferedImage(pgsize.width* times, pgsize.height* times, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setPaint(Color.white);
                    graphics.scale(times, times);// 将图片放大times倍
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                    // 最核心的代码
                    slide.draw(graphics);

                    //图片将要存放的路径
//                    String fileName="tmpimg_"+page+"_";
//                    File file = File.createTempFile(fileName+dateFormat.format(new Date()),".jpg");

                    String fileName="/Users/guomengqi/Desktop/ksk_crm/素材解析/imgs/tmpimg_"+page+"_"+dateFormat.format(new Date())+".png";
                    File file=new File(fileName);

                    // 图片路径存放
                    list.add(file.getAbsolutePath());
                    ImageIO.write(img, "JPEG", file);
                }
                logger.info("PPT转换成图片 成功，行数:{}",list.size());
                long end = new Date().getTime();
                log.info("ppt-to-images，耗时：{}s", ((end - start) / 1000.0) );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (slideShow != null) {
                    slideShow.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
