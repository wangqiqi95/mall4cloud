package com.mall4j.cloud.product.service;

import com.mall4j.cloud.product.vo.SpuExcelImportDataVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * spu信息
 */
public interface SpuReadExcelService {

	SpuExcelImportDataVO importParseSpus(MultipartFile multipartFile);

}
