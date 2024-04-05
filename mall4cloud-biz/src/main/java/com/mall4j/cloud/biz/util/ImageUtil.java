package com.mall4j.cloud.biz.util;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.exception.LuckException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class ImageUtil {

    /**
     * 将图片转为二进制数组
     *
     * @param imgUrl
     * @return
     */
    public static byte[] imgToBinary(String imgUrl) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imgUrl));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String suffix = imgUrlFileType(imgUrl);
            ImageIO.write(bufferedImage, suffix, baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            // 图片丢失，请重新上传图片
            throw new LuckException("图片丢失，请重新上传图片");
        }
    }

    /**
     * @param imgUrl http://img-test.gz-yami.com/2020/07/45d3631e97d8438d80f9db1369595b8c.jpg
     * @return 文件得后缀，文件类型 jpg ,  png , ...
     */
    public static String imgUrlFileType(String imgUrl) {
        if (StrUtil.isBlank(imgUrl)) {
            return imgUrl;
        }
        imgUrl.trim();
        String[] split = imgUrl.split("\\.");
        String s = split[split.length - 1];
        return s;
    }

    /**
     * @param imgUrl http://img-test.gz-yami.com/2020/07/45d3631e97d8438d80f9db1369595b8c.jpg
     * @return 获取文件名称 45d3631e97d8438d80f9db1369595b8c.jpg
     */
    public static String imgUrlFileName(String imgUrl) {
        if (StrUtil.isBlank(imgUrl)) {
            return imgUrl;
        }
        imgUrl.trim();
        String[] split = imgUrl.split("/");
        String s = split[split.length - 1];
        return s;
    }

    /**
     * @param imgUrl http://img-test.gz-yami.com/2020/07/45d3631e97d8438d80f9db1369595b8c.jpg
     * @return 获取文件名称 45d3631e97d8438d80f9db1369595b8c
     */
    public static String imgUrlFileNameNoSuffix(String imgUrl) {
        if (StrUtil.isBlank(imgUrl)) {
            return imgUrl;
        }
        imgUrl.trim();
        String[] split = imgUrl.split("/");
        String s = split[split.length - 1];
        String[] split1 = s.split("\\.");
        return split1[0];
    }
}
