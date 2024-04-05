package com.mall4j.cloud.biz.util;

import org.apache.commons.lang3.StringUtils;
import sun.awt.image.BufferedImageGraphicsConfig;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MergeImageUtil {


    private static class ImageType {
        private static final String DEFAULT_NAME = "image";
        private static final String PNG = "png";
        private static final String BASE64_PREFIX = "data:image/png;base64,";
    }

    /**
     * 将两个图片合并成一个图片【垂直合并】
     * @param first 图片1文件路径
     * @param second 图片2文件路径
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(String first, String second) throws IOException {
        FileInputStream img1 = new FileInputStream(first);
        FileInputStream img2 = new FileInputStream(second);
        BufferedImage image01 = ImageIO.read(img1);
        BufferedImage image02 = ImageIO.read(img2);
        return mergeImage(image01, image02, false);
    }

    /**
     * 将两个图片合并成一个图片【垂直合并】
     * @param first 图片1
     * @param second 图片2
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second) {
        return mergeImage(first, second, false);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal) {
        return mergeImage(first, second, horizontal, 0);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @param gap 图片之间的间距
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal, int gap) {
        return mergeImage(first, second, horizontal, false, gap);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @param center 图片是否水平居中, horizontal 等于 false 垂直合并才有效
     * @param gap 图片之间的间距
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal, boolean center, int gap) {
        return mergeImage(first, second, horizontal, center, false, gap, null, false, 0);
    }

    /**
     * 合并两个图片
     * @param first 图片1
     * @param second 图片2
     * @param horizontal 等于true,则两个图片水平合并显示, 否则两个图片垂直合并显示
     * @param center 图片是否水平居中, horizontal 等于 false 垂直合并才有效
     * @param transparent 合并后的图片背景是否透明色
     * @param gap 图片之间的间距
     * @param color 图片的背景颜色
     * @param secondRight 如果第二张图片比第一张图片小是否往右对齐
//     * @param font 字体模式
//     * @param remark 需要展示的文字
//     * @param remarkXRate 文字开始写入的x坐标（取x轴的百分比）
//     * @param remarkYRate 文字开始写入的y坐标（取y轴的百分比）
     * @return 返回合并之后的图片
     */
    public static BufferedImage mergeImage(BufferedImage first, BufferedImage second, boolean horizontal, boolean center,
                                           boolean transparent, int gap, Color color, boolean secondRight, int midGap) {
        if (first == null) {
            return second;
        } else if (second == null) {
            return first;
        }
        // 获取原始图片宽度
        int firstWidth = first.getWidth();
        int firstHeight = first.getHeight();
        int secondWidth = second.getWidth();
        int secondHeight = second.getHeight();
        int picGap = gap * 2;
        // 合并后的图片宽高
        int mergeWidth = Math.max(firstWidth, secondWidth) + picGap;
        int mergeHeight = firstHeight + secondHeight + picGap + midGap;
        if (horizontal) {
            mergeWidth = firstWidth + secondWidth + picGap;
            mergeHeight = Math.max(firstHeight, secondHeight) + picGap + midGap;
        }

        // 创建目标图片对象
        BufferedImage target = new BufferedImage(mergeWidth, mergeHeight, BufferedImage.TYPE_INT_RGB);
        if (transparent) {
            // 设置图片背景为透明的
            BufferedImageGraphicsConfig config = BufferedImageGraphicsConfig.getConfig(target);
            target = config.createCompatibleImage(mergeWidth, mergeHeight, Transparency.TRANSLUCENT);
        }

        // 创建绘制目标图片对象
        Graphics2D graphics = target.createGraphics();
        int x1, y1;
        int x2, y2;
        if (horizontal) {
            // 水平合并
            x1 = gap;
            y1 = gap;
            x2 = firstWidth + gap;
            y2 = gap;
        } else {
            // 垂直合并
            if (center) {
                // 计算居中位置
                x1 = (mergeWidth - firstWidth) / 2;
                x2 = (mergeWidth - secondWidth) / 2;
            } else {
                x1 = gap;
                int offset = firstWidth-secondWidth;
                if (secondRight && offset > 0){
                    x2 = offset;
                }else {
                    x2 = gap;
                }

            }
            y1 = gap;
            if (picGap>0){
                y2 = firstHeight + picGap;
            }else {
                y2 = firstHeight;
            }

        }

        // 图片的背景颜色
        if (color != null) {
            graphics.setColor(color);
            graphics.fillRect(0, 0, mergeWidth, mergeHeight);
        }

        // 按照顺序绘制图片
        graphics.drawImage(first, x1, y1, firstWidth, firstHeight, null);
        graphics.drawImage(second, x2+midGap, y2, secondWidth, secondHeight, null);
        graphics.dispose();

        // 返回合并后的图片对象
        return target;
    }

    /**
     * 为图片添加水印
     * @param target 目标图片
     * @param color 采用的字体颜色
     * @param font 字体格式
     * @param remark 水印文字
     * @param remarkX 填写水印的x轴位置
     * @param remarkY 填写水印的y轴位置
     * */
    public static BufferedImage mergeFont(BufferedImage target, Color color, Font font, String remark, float remarkX, float remarkY){
        if (font != null && StringUtils.isNotEmpty(remark)){
            BufferedImage watermarkTarget = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D watermarkGraphics = watermarkTarget.createGraphics();
            watermarkGraphics.drawImage(target, 0, 0, target.getWidth(), target.getHeight(), null);
            watermarkGraphics.setColor(color);
            watermarkGraphics.setFont(font);
            watermarkGraphics.drawString(remark, remarkX, remarkY);
            watermarkGraphics.dispose();
            return watermarkTarget;
        }
        return target;
    }

    /**
     * 保存合并后的图片到文件
     * @param fileName 文件路径名称
     * @param image 图片
     */
    public static void writeToFile(String fileName, BufferedImage image) throws IOException {
        if (fileName == null) {
            fileName = ImageType.DEFAULT_NAME;
        }
        File file = new File(fileName);
        // 保存图片
        ImageIO.write(image, ImageType.PNG, file);
    }

    /**
     * 图片转换成 Base64 编码字符串
     * @param image 图片
     * @return 返回转换之后的 base64 编码字符串
     */
    public static String writeToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 将图片写入字节输出流里面
        ImageIO.write(image, ImageType.PNG, bos);
        // 字节输出流转换成字节数组
        byte[] bytes = bos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encodeBuffer(bytes).trim();
        // 将字符串中的所有【\n】、【\r】删除
        base64 = base64.replaceAll("\n", "").replaceAll("\r", "");
        // 返回 base64 编码字符串
        return ImageType.BASE64_PREFIX + base64;
    }
}
