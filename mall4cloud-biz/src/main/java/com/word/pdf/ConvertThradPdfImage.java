package com.word.pdf;

import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class ConvertThradPdfImage extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ConvertThradPdfImage.class);

    private CountDownLatch countDownLatch;

    private PDFRenderer pdfRenderer;

    private int beginNumber;

    private int endNumber;

    private List<String> paths;

    public ConvertThradPdfImage(CountDownLatch countDownLatch,PDFRenderer pdfRenderer,List<String> paths,int beginNumber,int endNumber)
    {
        this.countDownLatch = countDownLatch;
        this.pdfRenderer = pdfRenderer;
        this.beginNumber = beginNumber;
        this.endNumber = endNumber;
        this.paths = paths;
    }

    public void run() {
        logger.info(this.getName() + "子线程开始");
        try {
            for (int i = beginNumber; i < endNumber; i++) {
                logger.info(this.getName() +"开始转换第"+i +"页图片,总页数:"+endNumber);
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 150, ImageType.RGB);
                RenderedImage rendImage = image;
                String path = paths.get(i);
                File file = new File(path);
                ImageIO.write(rendImage, "JPEG", file);
                image.flush();
            }
        } catch (Exception e) {
            logger.info("子线程结束【{}】转图片失败:{}",this.getName(),e);
        } finally {
            logger.info(this.getName() + "子线程结束");
            // 倒数器减1
            countDownLatch.countDown();
        }
    }
}
