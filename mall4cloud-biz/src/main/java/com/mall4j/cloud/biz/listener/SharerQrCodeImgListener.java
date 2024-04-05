package com.mall4j.cloud.biz.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.PageHelper;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.dto.channels.sharer.SharerQrCodeImgListReqDto;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSharerMapper;
import com.mall4j.cloud.biz.model.channels.LiveStoreSharer;
import com.mall4j.cloud.biz.util.CompressUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

/**
 * @Description 视频号导出分享员二维码
 * @Author axin
 * @Date 2023-02-27 10:43
 **/
@Slf4j
@Component
public class SharerQrCodeImgListener {
    @Autowired
    private ChannelsSharerMapper channelsSharerMapper;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;


    @EventListener(SharerQrCodeImgListReqDto.class)
    public void soldStoreEvent(SharerQrCodeImgListReqDto event) {
        FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
        try {
            log.info("--开始执行视频号分享员邀请二维码导出");
            event.setStatus(1);
            PageHelper.startPage(event.getPageNum(),event.getPageSize());
            List<LiveStoreSharer> liveStoreSharers = channelsSharerMapper.queryQrCodeImgList(event);
            if (CollectionUtil.isNotEmpty(liveStoreSharers)) {
                String time = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
                String fileName = time + "视频号邀请分享员二维码";
                String zipFilePathExport = "/opt/skechers/temp/tentacle/" + fileName + "/";
                new File(zipFilePathExport).mkdirs();

                for (LiveStoreSharer liveStoreSharer : liveStoreSharers) {
                    String name = zipFilePathExport + liveStoreSharer.getStaffNo() + liveStoreSharer.getName() + DateUtil.format(liveStoreSharer.getQrcodeImgExpireTime(),DatePattern.PURE_DATETIME_PATTERN) + ".png";
                    FileUtil.writeBytes(Base64Utils.decodeFromString(liveStoreSharer.getQrcodeImgBase64()), name);
                }

                File copyFile = new File(zipFilePathExport);
                String zipPath = CompressUtil.zipFile(copyFile, "zip");
                if (new File(zipPath).isFile()) {
                    //压缩统一文件目录
                    log.info("保存视频号邀请分享员二维码 zip zipPath【{}】", zipPath);
                    File zipFile = new File(zipPath);
                    FileInputStream fileInputStream = new FileInputStream(zipFile);
                    MultipartFile multipartFile = new MultipartFileDto(zipFile.getName(), zipFile.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                    String originalFilename = multipartFile.getOriginalFilename();
                    String mimoPath = "channels/tentacle/" + originalFilename;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    log.info("压缩包文件 zip url 【{}】", responseEntity.getData());

                    //备份下载中心
                    finishDownLoadDTO.setCalCount(liveStoreSharers.size());
                    finishDownLoadDTO.setFileName(fileName);
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    //删除本地文件
                    FileUtil.clean(zipFile);
                    FileUtil.clean(copyFile);
                    zipFile.delete();
                    copyFile.delete();
                }
            }
        } catch (Exception e) {
            finishDownLoadDTO.setStatus(2);
            log.error("导出视频号分享员二维码异常:{}",e);
        } finally {
            finishDownLoadDTO.setId(event.getDownLoadHisId());
            finishDownLoadDTO.setStatus(1);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.info("结束视频号分享员二维码导出");
        }
    }
}
