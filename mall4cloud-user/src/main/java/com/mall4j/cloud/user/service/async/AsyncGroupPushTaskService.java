package com.mall4j.cloud.user.service.async;

import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExportGroupPushTaskStatisticsBO;
import com.mall4j.cloud.user.bo.ExportSonTaskStaffPageBO;
import com.mall4j.cloud.user.bo.ExportStaffSendRecordBO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public interface AsyncGroupPushTaskService {

    @Async
    void wrapperExcelData(Long taskId, Long sonTaskId, HttpServletResponse response, FinishDownLoadDTO finishDownLoadDTO);


}
