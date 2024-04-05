package com.word.pdf;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PagesUtils {
    public static int getPageCount(int pages) {
        int i = pages / 3;
        if (i < 1) {
            return 1;
        } else {
            return i;
        }
    }

    public static List<String> getPaths(String dirPath,int pages) {
        List<String> imgList = new ArrayList<String>();
        try {
            for (int i = 0; i < pages; i++) {
                String path = "/"+i+"_";
                File file=null;
                if(StrUtil.isEmpty(dirPath)){
                    path = "/"+i+"_";
                    file=File.createTempFile(path,randomFileName("png"));
                }else{
                    path = dirPath + "/"+i+"_";
                    file=new File(path+randomFileName("jpg"));
                }
                imgList.add(file.getAbsolutePath());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return imgList;
    }

    /**
     *  随机生成不重复的随机数 时间戳加四位随机数带文件后缀
     * @return
     */
    public static String randomFileName(String suffix) {
        return System.currentTimeMillis()+""+(new Random().nextInt(9999)+1000)+"."+suffix;
    }
}
