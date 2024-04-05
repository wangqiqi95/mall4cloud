package com.mall4j.cloud.biz.feign;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.feign.ExcelUploadFeignClient;
import com.mall4j.cloud.biz.service.ExcelUploadService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Date 2022年02月14日, 0030 14:48
 * @Created by gmq
 */
@RestController
@RequiredArgsConstructor
public class ExcelUploadFeignController implements ExcelUploadFeignClient {

    @Resource
    private ExcelUploadService excelUploadService;

    @Override
    public void soleAndUploadExcel(ExcelUploadDTO excelUploadDTO) {
        //是否导出文件：0否 1是
        if(excelUploadDTO.getIsFile()==0){
            excelUploadService.soleAndUploadExcel(excelUploadDTO);
        }else if(excelUploadDTO.getIsFile()==1){
            excelUploadService.soleAndUploadExcelFile(excelUploadDTO);
        }
    }

    @Override
    public ServerResponseEntity<String> createAnduploadExcel(ExcelUploadDTO excelUploadDTO) {
        return excelUploadService.createAnduploadExcel(excelUploadDTO);
    }
}
