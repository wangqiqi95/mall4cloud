package com.mall4j.cloud.biz.wx.wx.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

public class QrCodeUtil {

    //编码格式,采用utf-8
    private static final String UNICODE = "utf-8";
    //图片格式
    public static final String FORMAT = "PNG";
    //二维码宽度像素pixels数量
    private static final int QRCODE_WIDTH = 512;
    //二维码高度像素pixels数量
    private static final int QRCODE_HEIGHT = 512;
    //LOGO宽度像素pixels数量
    private static final int LOGO_WIDTH = 128;
    //LOGO高度像素pixels数量
    private static final int LOGO_HEIGHT = 128;

    /**
     * 生成二维码图片
     * @param content 二维码内容
     * @param logoPath logo图片地址
     * @return
     * @throws Exception
     */
    public static BufferedImage createQrCode(String content, String logoPath) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, UNICODE);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        if (logoPath == null || "".equals(logoPath)) {
            return image;
        }

//        image=toGray(image);
        // 插入图片
        insertLogo(image, logoPath);
        return image;
    }

    public static BufferedImage toGray(BufferedImage srcImg){
        return new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(srcImg, null);
    }

    //在图片上插入LOGO
    //source 二维码图片内容
    //logoPath LOGO图片地址
    private static void insertLogo(BufferedImage source, String logoPath) throws Exception {
        Image src = ImageIO.read(new URL(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (width > LOGO_WIDTH) {
            width = LOGO_WIDTH;
        }
        if (height > LOGO_HEIGHT) {
            height = LOGO_HEIGHT;
        }
        Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        src = image;
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        setGraphics2D(graph);
        graph.setColor(Color.WHITE);
        graph.setBackground(Color.WHITE);//填充整个屏幕
        int x = (QRCODE_WIDTH - width) / 2;
        int y = (QRCODE_HEIGHT - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);

        graph.dispose();
    }

    /**
     * 设置 Graphics2D 属性 （抗锯齿）
     *
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setGraphics2D(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(s);
    }

    /**
     * 二维码图片插入logo
     * @param picUrl
     * @param logo
     * @return
     * @throws Exception
     */
    public static BufferedImage insertImageLogo(String picUrl, String logo) throws Exception {
        URL qrCodeImageUrl = new URL(picUrl.replace("https:","http:"));
        URLConnection con = qrCodeImageUrl.openConnection();
        InputStream inputStream = con.getInputStream();
        BufferedInputStream bytes = new BufferedInputStream(inputStream);
        BufferedImage image =ImageIO.read(bytes);
        image=toGray(image);
        insertLogo(image,logo);
        return image;
    }

    public static void main(String[] args) {
        try {
            String logo="https://ndwl.oss-cn-shanghai.aliyuncs.com/2024/01/30/dd084c44cbaa45fba95593b111da26df";
            String filePath="/Users/guomengqi/Downloads/code.png";
            File file=new File(filePath);
            ImageIO.write(createQrCode("http://ys-h5-scrm.morewarm.com/packageMember/pages/group/group?state=1706601879410013", logo), FORMAT, file);
//            String path = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getMediaService().uploadImg(file);
//            System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
