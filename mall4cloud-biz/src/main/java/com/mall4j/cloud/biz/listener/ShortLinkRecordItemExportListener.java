package com.mall4j.cloud.biz.listener;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.service.WeixinShortlinkService;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordItemVo;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.CompressUtil;
import com.mall4j.cloud.common.util.FileUtil;
import com.mall4j.cloud.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.http.entity.ContentType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description 触点记录明细导出
 * @Author tan
 * @Date 2022-12-29 10:22
 **/
@Slf4j
@Component
public class ShortLinkRecordItemExportListener {
    @Resource
    private WeixinShortlinkService weixinShortlinkService;
    @Resource
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Resource
    private MinioUploadFeignClient minioUploadFeignClient;
    @Resource
    private MapperFacade mapperFacade;

    @EventListener(classes = ShortLinkRecordItemExport.class)
    public void ShortLinkRecordItemExport(ShortLinkRecordItemExport shortLinkRecordItemExport) {
        ThreadUtil.execute(() -> {
            FinishDownLoadDTO finishDownLoadDTO = new FinishDownLoadDTO();
            try {
                WeixinShortlinkRecordItemPageDTO weixinShortlinkRecordItemPageDTO = shortLinkRecordItemExport.getWeixinShortlinkRecordItemPageDTO();
                PageVO<WeixinShortlinkRecordItemVo> weixinShortlinkRecordItemVoPageVO;
                int pageNo = 0;
                List<String> localFileUrls = Lists.newArrayList();
                do {
                    weixinShortlinkRecordItemPageDTO.setPageNum(pageNo++);
                    weixinShortlinkRecordItemPageDTO.setPageSize(5000);
                    weixinShortlinkRecordItemVoPageVO = weixinShortlinkService.selectShortLinkRecordItemPage(weixinShortlinkRecordItemPageDTO);
                    List<ShortLinkRecordItemExportRespDTO> itemExportRespDTOS = mapperFacade.mapAsList(weixinShortlinkRecordItemVoPageVO.getList(), ShortLinkRecordItemExportRespDTO.class);
                    String pathExport = SystemUtils.getExcelFilePath() + "/" + ShortLinkRecordItemExport.SHORT_LINK_RECORD_ITEM_DETAIL_FILE_NAME + "_" + SystemUtils.getExcelName() + ".xls";
                    EasyExcel.write(pathExport, ShortLinkRecordItemExportRespDTO.class).sheet(ExcelModel.SHEET_NAME).doWrite(itemExportRespDTOS);
                    localFileUrls.add(pathExport);
                } while (pageNo < weixinShortlinkRecordItemVoPageVO.getPages());

                String pathExport;
                String contentType;

                if (localFileUrls.size() == 1) {
                    pathExport = localFileUrls.get(0);
                    contentType = "application/vnd.ms-excel";
                } else {
                    //String time = DateUtil.formatDateTime(new Date());
                    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String zipFilePathExport = CompressUtil.ZIP_FILE_PATH_EXPORT + time + "/";
                    List<File> fromFileList = Lists.newArrayList();
                    File copyFile = new File(zipFilePathExport);
                    localFileUrls.forEach(item -> {
                        File file = new File(item);
                        fromFileList.add(file);
                        log.info(ShortLinkRecordItemExport.SHORT_LINK_RECORD_ITEM_DETAIL_FILE_NAME + "，单个excel文件信息 文件名【{}】 文件大小【{}】", file.getName(), cn.hutool.core.io.FileUtil.size(file));
                    });
                    copyFile.mkdirs();
                    //文件存放统一目录
                    FileUtil.copyCodeToFile(fromFileList, zipFilePathExport, Lists.newArrayList());
                    //压缩统一文件目录
                    pathExport = CompressUtil.zipFile(copyFile, "zip");
                    contentType = ContentType.APPLICATION_OCTET_STREAM.toString();
                }
                String fileUrl = upload(pathExport, contentType);
                finishDownLoadDTO.setFileUrl(fileUrl);
                finishDownLoadDTO.setCalCount(weixinShortlinkRecordItemVoPageVO.getTotal().intValue());
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setRemarks("导出成功");
            } catch (Exception e) {
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("导出失败");
                log.error(ShortLinkRecordItemExport.SHORT_LINK_RECORD_ITEM_DETAIL_FILE_NAME + "导出失败", e);
            } finally {
                finishDownLoadDTO.setId(shortLinkRecordItemExport.getDownloadCenterId());
                finishDownLoadDTO.setFileName(ShortLinkRecordItemExport.SHORT_LINK_RECORD_ITEM_DETAIL_FILE_NAME);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        });
    }


    private String upload(String pathExport, String contentType) {
        File file = null;
        try {
            file = new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(), contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess()) {
                log.info("上传文件地址：" + responseEntity.getData());
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            return responseEntity.getData();
        } catch (Exception e) {
            // 删除临时文件
            if (file != null && file.exists()) {
                file.delete();
            }
            return null;
        }
    }
}
