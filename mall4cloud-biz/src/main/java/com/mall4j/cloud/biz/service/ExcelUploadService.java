package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.api.biz.dto.ExcelUploadDTO;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 短信记录表
 *
 * @author lhd
 * @date 2021-01-04 13:36:52
 */
public interface ExcelUploadService {

	 void soleAndUploadExcel( ExcelUploadDTO excelUploadDTO);

	 void soleAndUploadExcelFile( ExcelUploadDTO excelUploadDTO);

	ServerResponseEntity<String> createAnduploadExcel(ExcelUploadDTO excelUploadDTO);
}
