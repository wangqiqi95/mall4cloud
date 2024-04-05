package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuSkuAttrValueVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.vo.SpuExcelVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SkuExcelService {

	void updateSkuBatchByPriceCode(List<Long> spuIds,List<Sku> skus,Long storeId,String fileName);

	SkuVO getSkuBySkuPriceCode(String priceCode);

	SpuVO getSpuByCode(String spuCode);

	/**
	 * 商品导入的错误信息
	 * @param errorMap
	 * @return
	 */
	String spuExportError(Map<Integer, List<String>> errorMap);

	String checkThreeLower(MultipartFile multipartFile);
}
