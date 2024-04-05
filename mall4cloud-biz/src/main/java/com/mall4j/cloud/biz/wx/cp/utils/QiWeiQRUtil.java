package com.mall4j.cloud.biz.wx.cp.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import com.mall4j.cloud.biz.util.SystemLoadFont;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.font.FontDesignMetrics;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @Date 2022年2月7日, 0007 14:36
 * @Created by eury
 */
@Slf4j
public class QiWeiQRUtil {

    //字符集
    private static final String CHARSET = "utf-8";
    public static final String FORMAT_NAME = "png";
    // 二维码尺寸
    private static int QRCODE_SIZE = 500;
    // 二维码尺寸
    private static int QRCODE_SIZE_HEIGHT = 700;

    // LOGO宽度
    private static  int WIDTH = 80;
    // LOGO高度
    private static  int HEIGHT = 80;

    private static  int TOP_FONT_SIZE = 23;
    private static  int TOP_FONT_HEIGHT = 24;
    private static  int UP_FONT_SIZE = 23;
    private static  int UP_FONT_HEIGHT = 24;

    private static String simsunPath="/opt/skechers/temp/wxaterial/font/simsun.ttc";
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     *
     * @return
     * @throws Exception
     */
    public static BufferedImage createImageAndLogoText(String picUrl, String topText,String storeText,String upText) throws Exception {
        URL qrCodeImageUrl = new URL(picUrl.replace("https:","http:"));
        URLConnection con = qrCodeImageUrl.openConnection();
        InputStream inputStream = con.getInputStream();
        BufferedInputStream bytes = new BufferedInputStream(inputStream);
        BufferedImage image =ImageIO.read(bytes);
        insertImage( image,"https://xcx-sit-uat.oss-cn-shanghai.aliyuncs.com/2022/04/16/afb4f9f9fa32442e85859f0a75e49059",   true,  396);

        BufferedImage outSideImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = outSideImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, QRCODE_SIZE, QRCODE_SIZE_HEIGHT);// 填充整个屏幕
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);      // 消除画图锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);   // 消除文字锯齿
        int height  = addStaffHeader(outSideImage,topText);
        //System.out.println("height:"+height);
        int storeHeight = addStoreHeader(outSideImage,storeText,height);
        Rectangle rectangle = new Rectangle(0, height+storeHeight+TOP_FONT_HEIGHT+TOP_FONT_HEIGHT, QRCODE_SIZE, QRCODE_SIZE);
        g.drawImage(image.getScaledInstance(rectangle.width, rectangle.height, Image.SCALE_SMOOTH), rectangle.x, rectangle.y, null);
        if(StringUtils.isNotEmpty(upText)){
            String[] upTexts=upText.split(",");
            int step=3;
            int up=UP_FONT_SIZE*(upTexts.length-1);
            int y=QRCODE_SIZE_HEIGHT - (20 * step) - up-40;
            for(int i=0;i<upTexts.length;i++){
                if(i>0){
                    y=y+UP_FONT_SIZE;
                }
                addFontImage(outSideImage, upTexts[i],step,y);
                step--;
            }
        }
        return outSideImage;
    }

    /**
     * 二维码图片插入logo
     * @param picUrl
     * @param logo
     * @return
     * @throws Exception
     */
    public static BufferedImage insertImageLogo(String picUrl, String logo) throws Exception {
        log.info("insertImageLogo--{}",picUrl);
        URL qrCodeImageUrl = new URL(picUrl.replace("https:","http:"));
        URLConnection con = qrCodeImageUrl.openConnection();
        InputStream inputStream = con.getInputStream();
        BufferedInputStream bytes = new BufferedInputStream(inputStream);
        BufferedImage image =ImageIO.read(bytes);
        image=toGray(image);
        insertLogo(image,logo);
        return image;
    }

    public static BufferedImage toGray(BufferedImage srcImg){
        return new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(srcImg, null);
    }

    private static void insertLogo(BufferedImage image,String logoFile)throws Exception{
        int width = image.getWidth();
        int height = image.getHeight();
        if (Objects.nonNull(logoFile)) {
            log.info("insertLogo--{}",logoFile);
            // 构建绘图对象
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            URL logurl = new URL(logoFile.replace("https:","http:"));
            BufferedImage logo = ImageIO.read(logurl);
            // 开始绘制logo图片
            g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
            g.dispose();
            logo.flush();
        }
    }

    private static void insertImage(BufferedImage source, String logoImgUrl, boolean needCompress,int qrcodeheight) throws Exception {
        Image src = ImageIO.read(new URL(logoImgUrl));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        Graphics2D graph = source.createGraphics();
        int x = (qrcodeheight - width) / 2;
        int y = (qrcodeheight - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(6f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 添加 头部图片文字
     * @param source      图片源
     * @param declareText 文字本文
     */
    private static int addStaffHeader(BufferedImage source, String declareText) {
        BufferedImage textImage = strToImage(declareText, QRCODE_SIZE, TOP_FONT_HEIGHT,TOP_FONT_SIZE);
        Graphics2D graph = source.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int width = textImage.getWidth(null);
        int height = textImage.getHeight(null);
        Image src = textImage;
        int y=10;
        graph.drawImage(src, 0, y, width, height, null);
        graph.dispose();
        return height;
    }

    /**
     * 添加 头部图片文字
     * @param source      图片源
     * @param declareText 文字本文
     */
    private static int addStoreHeader(BufferedImage source, String declareText,int startHeight) {
        BufferedImage textImage = strToImage(declareText, QRCODE_SIZE, TOP_FONT_HEIGHT,TOP_FONT_SIZE);
        Graphics2D graph = source.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int width = textImage.getWidth(null);
        int height = textImage.getHeight(null);
        Image src = textImage;
        int y=startHeight+TOP_FONT_HEIGHT+10;
        graph.drawImage(src, 0, y, width, height, null);
        graph.dispose();
        return height;
    }


    /**
     * 添加 底部图片文字
     *
     * @param source      图片源
     * @param declareText 文字本文
     */
    private static void addFontImage(BufferedImage source, String declareText,int step,int y) {
        BufferedImage textImage = strToImage(declareText, QRCODE_SIZE, UP_FONT_HEIGHT,UP_FONT_SIZE);
        Graphics2D graph = source.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = textImage.getWidth(null);
        int height = textImage.getHeight(null);

        Image src = textImage;
//        int y=QRCODE_SIZE_HEIGHT - (20 * step) - 20;
        if(QRCODE_SIZE<=300){
            y=QRCODE_SIZE_HEIGHT - (20 * step) - 20;
        }
        graph.drawImage(src, 0,y , width, height, null);
        graph.dispose();
    }

    @SuppressWarnings("restriction")
    private static BufferedImage strToImage(String str, int width, int height,int fontSize) {
        BufferedImage textImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)textImage.getGraphics();
        //开启文字抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.BLACK);
        FontRenderContext context = g2.getFontRenderContext();
        Font font = new Font(null, Font.PLAIN, fontSize);
        try {
            font = SystemLoadFont.styleFont(simsunPath,Font.PLAIN,fontSize);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        if(font==null){
            font = new Font(null, Font.PLAIN, fontSize);
        }
        g2.setFont(font);
        LineMetrics lineMetrics = font.getLineMetrics(str, context);
        FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
        float offset = (width - fontMetrics.stringWidth(str)) / 2;
        float y = (height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;

        g2.drawString(str, (int)offset, (int)y);

        return textImage;
    }

    public static String convert(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(image, "png", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return "data:image/png;base64,"+png_base64;
    }

    public static String convertByte(byte[] bytes) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return "data:image/png;base64,"+png_base64;
    }

    public static String splitStrLength(String item,int spLength){
        String regex = "(.{"+spLength+"})";//其中的12表示按照每12位进行分割
        item = item.replaceAll(regex, "$1,");
        return item;
    }

    public static void main(String[] strings){
        try {
            String content="https://wework.qpic.cn/wwpic3az/128464_lDRSpbrOQ9GKTqY_1709796629/0";
            String logo="https://cn-scrm-greenplum.s3.cn-north-1.amazonaws.com.cn/fea51e0c0885451495c482a65de46bd7";
            BufferedImage image=insertImageLogo(content,logo);
            String baseData=convert(image);
            System.out.println("------>"+baseData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
