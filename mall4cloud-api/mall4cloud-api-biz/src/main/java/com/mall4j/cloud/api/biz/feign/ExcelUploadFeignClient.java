package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author gmq
 * @Date 2022/03/22 15:19
 */
@FeignClient(value = "mall4cloud-biz",contextId = "soldexcelandupload")
public interface ExcelUploadFeignClient {

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/excel/soleAndUploadExcel")
    void soleAndUploadExcel(@RequestBody ExcelUploadDTO excelUploadDTO) ;

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/excel/createAnduploadExcel")
    ServerResponseEntity<String> createAnduploadExcel(@RequestBody ExcelUploadDTO excelUploadDTO) ;
}
