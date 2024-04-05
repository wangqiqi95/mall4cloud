package com.mall4j.cloud.biz.wx.cp.utils;

import cn.hutool.core.io.FileUtil;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.biz.wx.wx.util.QRUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {



    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static File creatTempImageFile(String fileName,String picUrl) throws IOException {
        File file = File.createTempFile(fileName,".png");
        URL qrCodeImageUrl = new URL(picUrl.replace("https:","http:"));
        URLConnection con = qrCodeImageUrl.openConnection();
        try{
            InputStream inputStream = con.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(inputStream);
             OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = bis.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            outputStream.flush();
        }catch (Exception e){
            log.error("",e);
        }
        return file;
    }

    public static File creatTempFile(String fileName,String picUrl,boolean randomFileName) throws IOException {
        File tempFile=new File(FileUtils.getTempDirectory().getAbsolutePath()+"/"+fileName);
        if(randomFileName){//是否文件名随机
            tempFile=File.createTempFile(fileName+dateFormat.format(new Date()),"."+FileUtil.getSuffix(fileName));
        }
        URL qrCodeImageUrl = new URL(picUrl.replace("https:","http:"));
        URLConnection con = qrCodeImageUrl.openConnection();
        try{
            InputStream inputStream = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = bis.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            outputStream.flush();
        }catch (Exception e){
            log.error("",e);
        }
        return tempFile;
    }

    public static void main(String[] s ) throws Exception{
        String name="云POS促销设置指南 V1.1.pptx";
        String fileName=FileUtil.getPrefix(name);
        String suffix=FileUtil.getSuffix(name);
//        File file = File.createTempFile(fileName,"."+suffix);
        File file = FileUtils.getTempDirectory();
        File tempFile=new File(file.getAbsolutePath()+"/"+name);
        System.out.println(file.getAbsolutePath());
        System.out.println(tempFile.getAbsolutePath());
    }

    public static File creatTempImageLogoFile(String fileName,String picUrl,String logo) throws IOException {
        File file = File.createTempFile(fileName+dateFormat.format(new Date()),".png");
        try{
            BufferedImage bufferedImage=QiWeiQRUtil.insertImageLogo(picUrl,logo);
            OutputStream outputStream = new FileOutputStream(file);
            ImageIO.write(bufferedImage, QRUtil.FORMAT_NAME, outputStream);
            outputStream.flush();
        }catch (Exception e){
            log.error("",e);
        }
        return file;
    }


    public static File creatTempImageFile(StoreVO storeVO, StaffVO staffVO, String picUrl) throws IOException {
        File file = File.createTempFile(storeVO.getName()+"-"+staffVO.getStaffName()+"-"+staffVO.getStaffNo()+"-",".png");
        try{
            String staffTxt=staffVO.getStaffName()+" | "+staffVO.getStaffNo();
            String storeText=storeVO.getName() ;
            String upText="微信\"扫一扫\"，领取会员卡";
            int spLength=14;
            upText= QRUtil.splitStrLength(upText,spLength);
            BufferedImage bufferedImage=QiWeiQRUtil.createImageAndLogoText(picUrl,staffTxt,storeText,upText);
            OutputStream outputStream = new FileOutputStream(file);
            ImageIO.write(bufferedImage, QRUtil.FORMAT_NAME, outputStream);
            outputStream.flush();
        }catch (Exception e){
            log.error("",e);
        }
        return file;
    }

    public static void downLoadImageLogo(String fileName,String picUrl,String logo,HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream");
        fileName= URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename="+fileName+".png");
        response.setCharacterEncoding("utf-8");

        try {
            BufferedImage bufferedImage=QiWeiQRUtil.insertImageLogo(picUrl,logo);
            ImageIO.write(bufferedImage, QRUtil.FORMAT_NAME, response.getOutputStream());
        }catch (Exception e){
            log.error("--downLoadImageLogo--{}",e);
        }
    }



    public static void downLoadImage(String fileName,String picUrl,HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream");
        fileName= URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename="+fileName+".png");
        response.setCharacterEncoding("utf-8");

        try {
            URL qrCodeImageUrl = new URL(picUrl.replace("https:","http:"));
            URLConnection con = qrCodeImageUrl.openConnection();
            InputStream inputStream = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = bis.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            outputStream.flush();
        }catch (Exception e){
            log.error("",e);
        }
    }



    public static void downLoadImage(StoreVO storeVO, StaffVO staffVO, String picUrl,  HttpServletResponse response) {
        try{
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition","attachment; filename="+new String((storeVO.getName()+"-"+staffVO.getStaffName()+"-"+staffVO.getStaffNo()).getBytes("UTF-8"), "ISO8859-1")+".png");
            String staffTxt=staffVO.getStaffName()+" | "+staffVO.getStaffNo();
            String storeText=storeVO.getName() ;
            String upText="微信\"扫一扫\"，领取会员卡";
            int spLength=14;
            upText= QRUtil.splitStrLength(upText,spLength);
            BufferedImage bufferedImage=QiWeiQRUtil.createImageAndLogoText(picUrl,staffTxt,storeText,upText);
            ImageIO.write(bufferedImage, QRUtil.FORMAT_NAME, response.getOutputStream());
        }catch (Exception e){
            log.error("",e);
        }
    }

    public static void downloadZip(String zipName,List<File> fileList, HttpServletResponse response) throws IOException {
        File zipFile = File.createTempFile(zipName+dateFormat.format(new Date()),".zip");
        try ( FileOutputStream fousa = new FileOutputStream(zipFile);
              ZipOutputStream zipOut = new ZipOutputStream(fousa);)
        {
            ZipUtils.zipFile(fileList, zipOut);

        }  catch (Exception e){
            log.error("--downloadZip--{}",e);
        }
        // 清空response
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipFile.getName().getBytes("GB2312"), "ISO8859-1"));
        try ( OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
              InputStream fis = new BufferedInputStream(new FileInputStream(zipFile));)
        {
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = fis.read(buffer))!=-1){
                toClient.write(buffer,0,len);
            }
            toClient.flush();
        }catch (Exception e){
            log.error("--downloadZip--{}",e);
        }
    }



    /**
     * 把接受的全部文件打成压缩包
     * @param files;
     * @param outputStream
     */
    public static void zipFile (List files, ZipOutputStream outputStream) {
        int size = files.size();
        for(int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }
    /**
     * 根据输入的文件与输出流对文件进行打包
     * @param inputFile
     * @param ouputStream
     */
    public static void zipFile(File inputFile,  ZipOutputStream ouputStream) {
        try {
            if(inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}