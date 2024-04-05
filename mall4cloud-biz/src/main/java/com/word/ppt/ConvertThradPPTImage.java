package com.word.ppt;

import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ConvertThradPPTImage extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ConvertThradPPTImage.class);

    private CountDownLatch countDownLatch;

    private List<Slide> slides;
    private Dimension pgsize;
    private int times;

    private int beginNumber;

    private int endNumber;

    private List<String> paths;

    public ConvertThradPPTImage(CountDownLatch countDownLatch, List<Slide> slides, List<String> paths,Dimension pgsize,int times, int beginNumber, int endNumber)
    {
        this.countDownLatch = countDownLatch;
        this.slides = slides;
        this.beginNumber = beginNumber;
        this.endNumber = endNumber;
        this.paths = paths;
        this.pgsize = pgsize;
        this.times = times;
    }

    public void run() {
        logger.info(this.getName() + "子线程开始");
        try {
            for (int i = beginNumber; i < endNumber; i++) {
                logger.info(this.getName() +"开始转换第"+i +"页图片");
                createImage(i,slides.get(i),paths);
            }
        } catch (Exception e) {
            logger.info("子线程结束【{}】转图片失败:{}",this.getName(),e);
        } finally {
            logger.info(this.getName() + "子线程结束");
            // 倒数器减1
            countDownLatch.countDown();
        }
    }

    private void createImage(int page,Slide slide,List<String> list){
        try {
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
                                xslfTextRun.setFontSize(xslfTextRun.getFontSize()-3);
                                xslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
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
            String path = paths.get(page);
            File file = new File(path);
            ImageIO.write(img, "JPEG", file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
