package com.mall4j.cloud.biz.wx.wx.util;

import java.awt.*;
import java.io.*;
import java.awt.image.*;

import cn.hutool.core.io.FileUtil;
import com.mall4j.cloud.biz.util.SystemLoadFont;
import lombok.extern.slf4j.Slf4j;
import javax.imageio.*;

/**
 * 小程序太阳码增加文字信息
 * @Date 2022年5月14日, 0014 11:00
 * @Created by eury
 */
@Slf4j
public class MaSunCodeUtils {

    private static String simsunPath="/opt/skechers/temp/wxaterial/font/simsun.ttc";//字体包
    private static  int FONT_SIZE = 15;//文字大小

    public static File ImgYin(String s,String ImgName,int width_y,int fontSize){
        if(width_y<=0){
            width_y = 110;
        }
        if(fontSize>0){
            FONT_SIZE = fontSize;
        }
        long startTime = System.currentTimeMillis();
        try{
            log.info("开始执行图片增加文字 文字【{}】 图片位置【{}】",s,ImgName);
            String str = s;
            File _file = new File(ImgName);
            Image src = ImageIO.read(_file);
            int width=src.getWidth(null);
            int height=src.getHeight(null);
            BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            Graphics2D  g=image.createGraphics();
            // ---------- 背景透明代码结束 -----------------
            g.drawImage(src,0,0,width,height,null);
            //字体颜色
            g.setColor(Color.black);
            //------------字体包--------------------------
            Font font = new Font(null, Font.PLAIN, FONT_SIZE);
            try {
                font = SystemLoadFont.styleFont(simsunPath,Font.PLAIN,FONT_SIZE);
            }catch (Exception e){
                log.info(e.getMessage());
            }
            if(font==null){
                font = new Font(null, Font.PLAIN, FONT_SIZE);
            }
            g.setFont(font);
            //------------字体包--------------------------
            //字体所在位置
            g.drawString(str,width-width_y,height-10);
            //结束会
            g.dispose();
            //重新生成图片
            ImageIO.write(image, "png", _file);
            log.info("结束执行图片增加文字，耗时：{}ms", System.currentTimeMillis() - startTime);
            return _file;
        }catch(Exception e){
            e.printStackTrace();
            log.error("执行图片增加文字报错【{}】",e);
        }
        return null;
    }

    public static void main(String[] args){

        try {
            File bufferedImage=ImgYin("L322U128" , "C:\\Users\\gcold\\Desktop\\L322U128.png",70,14);
            String baseString= QRUtil.convertByte(FileUtil.readBytes(bufferedImage));
            System.out.println(baseString);
//        ImgYin("ZZXD-A-DF-03" , "C:\\Users\\gcold\\Desktop\\27364_111111.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }


//        try {
//            Image src = ImageIO.read(FileUtil.file("C:\\Users\\gcold\\Desktop\\27364_111111.jpg"));
//            int width=src.getWidth(null);
//            int height=src.getHeight(null);
//
//            ImgUtil.pressText(//
//                    FileUtil.file("C:\\Users\\gcold\\Desktop\\27364_111111.jpg"), //
//                    FileUtil.file("C:\\Users\\gcold\\Desktop\\27364_111111_update.jpg"), //
//                    "SM0006", Color.BLACK, //文字
//                    new Font("黑体", Font.BOLD, 20), //字体
//                    width/2-80, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
//                    height/2, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
//                    0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
//            );
//        }catch (Exception e){
//            e.printStackTrace();
//        }


    }
}
