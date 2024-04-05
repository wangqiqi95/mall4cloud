package com.mall4j.cloud.biz.listener;

import cn.hutool.core.io.FileUtil;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.wx.wx.util.MaSunCodeUtils;
import com.mall4j.cloud.biz.dto.QrcodeTicketSpu;
import com.mall4j.cloud.biz.dto.QrcodeTicketSpuStoreDTO;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.biz.util.CompressUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.NumberTo64;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 门店商品二维码导出
 * @Date 2022年9月05日, 0027 14:25
 * @Created by eury
 */
@Slf4j
@Component("SoldStoreSpuCodeImageListener")
public class SoldStoreSpuCodeImageListener {

    @Autowired
    private QrcodeTicketService qrcodeTicketService;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Async
    @EventListener(QrcodeTicketSpuStoreDTO.class)
    public void soldStoreEvent(QrcodeTicketSpuStoreDTO event) {

        log.info("--开始执行门店商品二维码导出----总数【{}】",event.getSpuIds().size());
        Long startTime=System.currentTimeMillis();
        try {
            int i=0;
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName=time+"_商品二维码_"+event.getStoreCode();
            String zipFilePathExport="/opt/skechers/temp/tentacle/"+fileName+"/";
            new File(zipFilePathExport).mkdirs();
            log.info("小程序码(spu)压缩包 商品总数【{}】 门店id【{}】 path:【{}】 生成文件位置【{}】",event.getSpuIds().size(),event.getStoreId(),event.getPath(),zipFilePathExport);
            for(QrcodeTicketSpu ticketSpu:event.getSpuIds()){
                i++;
                log.info("小程序码(spu)压缩包 当前第{}个 spuCode:{}",i,ticketSpu.getSpu_code());
                String scene="id="+ticketSpu.getSpu_id()+"&s="+ NumberTo64.to64(event.getStoreId());
                ServerResponseEntity<byte[]> response=qrcodeTicketService.getWxaCodeByte(scene,event.getPath(),event.getWidth());
                if(response.isSuccess()){
                    String name=zipFilePathExport+event.getStoreCode()+"_"+ticketSpu.getSpu_code()+".png";
                    File file= FileUtil.writeBytes(response.getData(),name);
                    //生成的太阳码增加门店code信息
                    MaSunCodeUtils.ImgYin("  "+ticketSpu.getSpu_code(),file.getPath(), Objects.nonNull(event.getWidth_y())?event.getWidth_y():105,0);
                }
            }

            File copyFile=new File(zipFilePathExport);
            String zipPath= CompressUtil.zipFile(copyFile,"zip");
            if(new File(zipPath).isFile()){
                //压缩统一文件目录
                log.info("保存微信触点二维码 zip zipPath【{}】",zipPath);
                File zipFile=new File(zipPath);
                FileInputStream fileInputStream = new FileInputStream(zipFile);
                MultipartFile multipartFile = new MultipartFileDto(zipFile.getName(), zipFile.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                String originalFilename = multipartFile.getOriginalFilename();
                String mimoPath = "wxqrcode/tentacle/" + originalFilename;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                log.info("压缩包文件 zip url 【{}】",responseEntity.getData());

                //备份下载中心
                FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
                finishDownLoadDTO.setCalCount(event.getSpuIds().size());
                finishDownLoadDTO.setId(event.getDownLoadHisId());
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(responseEntity.getData() == null ? 2 : 1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);

                //删除本地文件
                File file=new File(zipPath);
                FileUtil.clean(file);
                FileUtil.clean(copyFile);
                file.delete();
                copyFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("-----结束执行门店商品二维码导出------耗时：{} ms", (System.currentTimeMillis() - startTime));
    }
}
