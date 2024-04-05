package com.mall4j.cloud.user.service.async.impl;

import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExportGroupPushTaskStatisticsBO;
import com.mall4j.cloud.user.bo.ExportSonTaskStaffPageBO;
import com.mall4j.cloud.user.bo.ExportStaffSendRecordBO;
import com.mall4j.cloud.user.manager.GroupPushTaskManager;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.service.GroupPushTaskStaffRelationService;
import com.mall4j.cloud.user.service.GroupSonTaskSendRecordService;
import com.mall4j.cloud.user.service.async.AsyncGroupPushTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AsyncGroupPushTaskServiceImpl implements AsyncGroupPushTaskService {

    @Autowired
    private GroupPushTaskStaffRelationService groupPushTaskStaffRelationService;

    @Autowired
    private GroupSonTaskSendRecordService groupSonTaskSendRecordService;

    @Autowired
    private GroupPushTaskManager groupPushTaskManager;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private GroupPushTaskService groupPushTaskService;


    @Async
    @Override
    public void wrapperExcelData(Long taskId, Long sonTaskId, HttpServletResponse response, FinishDownLoadDTO finishDownLoadDTO) {
        try {
            ExportGroupPushTaskStatisticsBO exportStatistic = groupPushTaskService.getExportStatistic(taskId, sonTaskId);
            List<ExportGroupPushTaskStatisticsBO> groupPushTaskStatisticsBOList = Arrays.asList(exportStatistic);

            List<ExportSonTaskStaffPageBO> exportSonTaskStaffPageBOList = groupPushTaskStaffRelationService.getDataByTaskAndSonTask(taskId, sonTaskId);
            List<ExportStaffSendRecordBO> sendRecordBOList = groupSonTaskSendRecordService.getDataByTaskAndSonTask(taskId, sonTaskId);


            String excelPath = groupPushTaskManager
                    .createExcelFile(groupPushTaskStatisticsBOList, exportSonTaskStaffPageBOList,sendRecordBOList,finishDownLoadDTO.getFileName());

            //填充数据到下载中心
            File file = new File(excelPath);
            if(file.isFile()){
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                FileInputStream fileInputStream = new FileInputStream(excelPath);
                String contentType = "application/vnd.ms-excel";
                MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                        contentType, fileInputStream);
                String originalFilename = multipartFile.getOriginalFilename();
                String mimoPath = "excel/" + time + "/" + originalFilename;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                if(responseEntity.isSuccess()){
                    log.info("---ExcelUploadService---" + responseEntity.toString());
                    //下载中心记录
                    int size = groupPushTaskStatisticsBOList.size() + exportSonTaskStaffPageBOList.size() + sendRecordBOList.size();
                    finishDownLoadDTO.setCalCount(size);
                    finishDownLoadDTO.setStatus(1);
                    finishDownLoadDTO.setFileUrl(responseEntity.getData());
                    finishDownLoadDTO.setRemarks("导出成功");
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                }
                //删除本地临时文件
                cn.hutool.core.io.FileUtil.del(excelPath);
            }
        }catch (Exception e){
            log.error("会员礼物活动会员名单导出异常，message is:{}",e);

            //下载中心记录
            if(finishDownLoadDTO!=null){
                finishDownLoadDTO.setStatus(2);
                finishDownLoadDTO.setRemarks("触达数据导出，excel生成zip失败");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            }
        }
    }
}
