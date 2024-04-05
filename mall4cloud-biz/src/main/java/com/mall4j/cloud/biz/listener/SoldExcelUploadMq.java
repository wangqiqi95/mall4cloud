package com.mall4j.cloud.biz.listener;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.biz.service.ExcelUploadService;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * excel导出且上传oss
 *
 * @gmq
 * @create 2022-03-22  16:44
 **/
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SOLD_UPLOAD_EXCEL, consumerGroup = "GID_"+RocketMqConstant.SOLD_UPLOAD_EXCEL)
public class SoldExcelUploadMq implements RocketMQListener<ExcelUploadDTO> {

    @Resource
    private ExcelUploadService excelUploadService;

    /**
     * excel导出且上传oss
     */
    @Override
    public void onMessage(ExcelUploadDTO excelUploadDTO) {
        //是否导出文件：0否 1是
        if(excelUploadDTO.getIsFile()==0){
            excelUploadService.soleAndUploadExcel(excelUploadDTO);
        }else if(excelUploadDTO.getIsFile()==1){
            excelUploadService.soleAndUploadExcelFile(excelUploadDTO);
        }
    }
}
