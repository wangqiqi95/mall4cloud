package com.word.pdf;

import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class PdfToImageUtil {

    public static void main(String[] strings){
        String pdfPath="/Users/guomengqi/Desktop/ksk_crm/素材解析/周报20231124.pdf";
        String dirPath="/Users/guomengqi/Desktop/ksk_crm/素材解析/imgs";
        List<String>  images=pdf2imgs(pdfPath,dirPath);
        System.out.println(images.toString());
    }

    /**
     * pdf转图片【多线程处理】
     * @param pdfPath pdf文件路径
     * @param dirPath 转出图片存储路径，传空则默认路径
     */
    public static List<String> pdf2imgs(String pdfPath, String dirPath) {
        try {
            log.info("开始执行pdf转图片");
            long start = new Date().getTime();
            PDDocument document = PDDocument.load(FileUtils.readFileToByteArray(new File(pdfPath)));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pages = document.getNumberOfPages();
            List<String> imgList = PagesUtils.getPaths(dirPath,pages);
            // 创建一个初始值为5的倒数计数器
            int downLatch=3;
            if(pages<downLatch){
                downLatch=pages;
            }
            CountDownLatch countDownLatch = new CountDownLatch(downLatch);
            for(int i = 0; i < downLatch; i++) {
                int begin = i*PagesUtils.getPageCount(pages);
                int end = (i+1)*PagesUtils.getPageCount(pages);
                if (begin >= end) {
                    end = begin;
                }
                if (i == 2) {
                    Thread thread = new ConvertThradPdfImage(countDownLatch, pdfRenderer, imgList,begin,pages);
                    thread.start();
                } else {
                    Thread thread = new ConvertThradPdfImage(countDownLatch, pdfRenderer, imgList,begin,end);
                    thread.start();
                }
            }
            // 阻塞当前线程，直到倒数计数器倒数到0
            countDownLatch.await();
            long end = new Date().getTime();
            log.info("pdf-to-images，耗时：{}s", ((end - start) / 1000.0) );
            return imgList;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * pdf转图片【单线程处理】
     * @param localFile
     * @return
     */
    public static List<String> convertPDFToImages(File localFile) {
        try {

            log.info("开始执行pdf转图片");
            long start = new Date().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            PDDocument document = PDDocument.load(FileUtils.readFileToByteArray(localFile));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            List<String> images = new ArrayList<>();
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
                String fileName="tmpimg_"+page+"_";
                File file = File.createTempFile(fileName+dateFormat.format(new Date()),".jpg");
                ImageIO.write(image, "JPEG", file);
                images.add(file.getAbsolutePath());
                log.info("pdf-to-images success -> path:{}",file.getAbsolutePath());
            }
            document.close();
            long end = new Date().getTime();
            log.info("pdf-to-images，耗时：{}s", ((end - start) / 1000.0) );
            return images;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("convertPDFToImages error : {}",e);
            throw new LuckException("操作失败");
        }
    }

}
