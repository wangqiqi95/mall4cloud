package com.mall4j.cloud.product.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.SkqUtils;
import com.mall4j.cloud.common.vo.UploadExcelVO;
import com.mall4j.cloud.product.event.SoldSpuEvent;
import com.mall4j.cloud.product.event.UpdateSkusPricefeeEvent;
import com.mall4j.cloud.product.manager.UpdateSkuPirceStatusManager;
import com.mall4j.cloud.product.service.SpuExcelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 商品导出
 * @Date 2022年4月27日, 0027 14:25
 * @Created by eury
 */
@Slf4j
@Component("UpdateSkusPricefeeListener")
@AllArgsConstructor
public class UpdateSkusPricefeeListener {

    @Autowired
    private UpdateSkuPirceStatusManager updateSkuPirceStatusManager;

    @Async
    @EventListener(UpdateSkusPricefeeEvent.class)
    public void soldSpuEvent(UpdateSkusPricefeeEvent event) {

        log.info("--开始执行商品批量改价----"+event.getFile());
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(event.getDownLoadHisId());
        File file=event.getFile();
        try {
            FileInputStream is = new FileInputStream(file);
            String contentType = "application/vnd.ms-excel";
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            updateSkuPirceStatusManager.updateSkuPirceExcel(multipartFile,event.getDownLoadHisId());

            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            finishDownLoadDTO.setRemarks("执行商品批量改价 soldSpuEvent，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            log.error("执行商品批量改价错误: "+e.getMessage(),e);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }


    }
}
