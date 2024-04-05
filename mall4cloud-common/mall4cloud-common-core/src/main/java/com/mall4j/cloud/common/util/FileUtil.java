package com.mall4j.cloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class FileUtil {

    public static File transferFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        //临时文件
        File file = null;
        try {
            file = File.createTempFile(filename[0], "." + filename[1]);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 根据url链接获取文件
     *
     * @param urlPath
     * @param method
     * @return
     */
    public static File URLFile(String urlPath, String method) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);
            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod(method);
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            System.out.println(httpURLConnection.getContentLength());
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 控制台打印文件大小
            System.out.println("您要下载的文件大小为:" + fileLength / (1024 * 1024) + "MB");

            // 建立链接从请求中获取数据
            URLConnection con = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 指定文件名称(有需求可以自定义)
            String fileFullName = "temp.xlsx";
            // 指定存放位置(有需求可以自定义)
            String path = System.getProperty("user.dir") + "/tempExcel/" + fileFullName;
            file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 控制台打印文件下载的百分比情况
                System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
            }
            // 关闭资源
            bin.close();
            out.close();
            System.out.println("文件下载成功！");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("文件下载失败！");
        } finally {
            return file;
        }
    }


    public static void copyCodeToFile(List<File> fromFileList, String zipFilePathExport, List<File> backFileList){
        try {
            for(File fromFile:fromFileList){
                String originalFilename=fromFile.getName();
                int pointIndex = -1;
                if (StringUtils.isNotBlank(originalFilename)) {
                    pointIndex = originalFilename.lastIndexOf(".");
                }
//                String ext = pointIndex != -1 ? originalFilename.substring(pointIndex) : "";
                String saveFileName=zipFilePathExport+""+originalFilename;
                File outFile=new File(saveFileName);
                FileCopyUtils.copy(fromFile,outFile);
                backFileList.add(outFile);
                //删除本地文件
                cn.hutool.core.io.FileUtil.clean(fromFile);
                fromFile.delete();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
