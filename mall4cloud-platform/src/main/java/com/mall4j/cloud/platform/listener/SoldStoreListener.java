package com.mall4j.cloud.platform.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.vo.SoldTzStoreVO;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.SystemUtils;
import com.mall4j.cloud.platform.event.SoldStoreEvent;
import com.mall4j.cloud.platform.service.TzStoreService;
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
 * 门店导出
 * @Date 2022年4月27日, 0027 14:25
 * @Created by eury
 */
@Slf4j
@Component("SoldStoreListener")
@AllArgsConstructor
public class SoldStoreListener {

    @Autowired
    private TzStoreService storeService;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Async
    @EventListener(SoldStoreEvent.class)
    public void soldStoreEvent(SoldStoreEvent event) {

        log.info("--开始执行门店导出----");

        Long downLoadHisId= event.getDownLoadHisId();
        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downLoadHisId);

        List<SoldTzStoreVO> soldTzStoreVOS = storeService.soldStore(event.getStoreQueryParamDTO(),null);
        if(CollUtil.isEmpty(soldTzStoreVOS)) {
            finishDownLoadDTO.setRemarks("没有可导出的数据");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            log.error("没有可导出的数据");
            return;
        }
        File file=null;
        try {
            int calCount=soldTzStoreVOS.size();
            log.info("导出数据行数 【{}】",calCount);

            long startTime = System.currentTimeMillis();
            log.info("开始执行门店生成excel 总条数【{}】",calCount);
            String pathExport= SystemUtils.getExcelFilePath()+"/"+ SystemUtils.getExcelName()+".xls";

            EasyExcel.write(pathExport, SoldTzStoreVO.class).sheet(ExcelModel.SHEET_NAME).doWrite(soldTzStoreVOS);

                    // 自定义合并规则
//                    .registerWriteHandler(new ExcelMergeHandler(SoldTzStoreVO.MERGE_ROW_INDEX, SoldTzStoreVO.MERGE_COLUMN_INDEX))
                    // 宽度自适应
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
//                    .sheet(ExcelModel.SHEET_NAME).doWrite(soldSpuExcels);

            String contentType = "application/vnd.ms-excel";
            log.info("结束执行门店生成excel，耗时：{}ms   总条数【{}】  excel本地存放目录【{}】", System.currentTimeMillis() - startTime,soldTzStoreVOS.size(),pathExport);

            startTime = System.currentTimeMillis();
            log.info("导出数据到本地excel，开始执行上传excel.....");
            file=new File(pathExport);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                    contentType, is);

            String originalFilename = multipartFile.getOriginalFilename();
            String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String mimoPath = "excel/" + time + "/" + originalFilename;
            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
            if (responseEntity.isSuccess() && finishDownLoadDTO!=null) {
                log.info("上传文件地址："+responseEntity.getData());
                //下载中心记录
                finishDownLoadDTO.setCalCount(calCount);
                String fileName= DateUtil.format(new Date(),"yyyyMMddHHmmss") +""+SoldTzStoreVO.EXCEL_NAME;
                finishDownLoadDTO.setFileName(fileName);
                finishDownLoadDTO.setStatus(1);
                finishDownLoadDTO.setFileUrl(responseEntity.getData());
                finishDownLoadDTO.setRemarks("导出成功");
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                // 删除临时文件
                if (file.exists()) {
                    file.delete();
                }
            }
            log.info("导出数据到本地excel，结束执行上传excel，耗时：{}ms", System.currentTimeMillis() - startTime);
        }catch (Exception e){
            log.error("导出门店信息错误: "+e.getMessage(),e);
            finishDownLoadDTO.setRemarks("导出门店失败，信息错误："+e.getMessage());
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            // 删除临时文件
            if (file!=null && file.exists()) {
                file.delete();
            }
        }
    }
}
