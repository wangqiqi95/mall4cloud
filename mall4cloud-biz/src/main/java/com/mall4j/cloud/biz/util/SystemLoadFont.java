package com.mall4j.cloud.biz.util;

import cn.hutool.core.lang.Assert;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * @Date 2022年3月10日, 0010 17:40
 * @Created by eury
 */
public class SystemLoadFont {
    /**
     * 本地读取方法
     * @param path 文件路径
     * @param style 字体样式
     * @param fontSize 字体大小
     * @return
     */
    public static Font styleFont(String path, int style, float fontSize) {
        Assert.notNull(path);
        Font font = SystemLoadFont.loadStyleFont(path,style,fontSize);// 调用
        return font;
    }
    /**
     *
     * @param fontFileName 外部字体名
     * @param style 字体样式
     * @param fontSize 字体大小
     * @return
     */
    public static Font loadStyleFont(String fontFileName,int style, float fontSize) {
        try{
            File file = new File(fontFileName);
            FileInputStream in = new FileInputStream(file);
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, in);
            Font dynamicFontPt =  dynamicFont.deriveFont(style,fontSize);
            in.close();
            return dynamicFontPt;
        }catch(Exception e) {//异常处理
            e.printStackTrace();
            return new Font("宋体", Font.PLAIN, (int)fontSize);
        }
    }
}

