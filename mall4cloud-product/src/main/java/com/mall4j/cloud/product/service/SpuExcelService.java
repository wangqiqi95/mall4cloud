package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.product.vo.SoldSpuExcelVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.SpuPageSearchDTO;
import com.mall4j.cloud.product.vo.SimpleSpuExcelVO;
import com.mall4j.cloud.product.vo.SpuExcelImportDataVO;
import com.mall4j.cloud.product.vo.SpuExcelVO;
import com.mall4j.cloud.product.vo.SpuPageVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * spu信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SpuExcelService {

	/**
	 * 获取spu信息列表（excel导出）
	 * @param spuDTO
	 * @return
	 */
    List<SoldSpuExcelVO> excelSpuList(SpuPageSearchDTO spuDTO);

	/**
	 * 导入商品
	 * @param spuList
	 * @param errorMap
	 */
	void exportExcel(List<SpuExcelVO> spuList, Map<Integer, List<String>> errorMap);

	/**
	 * 商品导入的错误信息
	 * @param errorMap
	 * @return
	 */
	String spuExportError(Map<Integer, List<String>> errorMap);

	/**
	 * 导出模板
	 * @param response
	 */
	void downloadModel(HttpServletResponse response);

	/**
	 *
	 * @param searchDTO
	 * @return
	 */
	List<SpuPageVO> listSpuByCodes(SpuPageSearchDTO searchDTO);

	List<SimpleSpuExcelVO> listSimpleSpuExcelVO();

	ServerResponseEntity<List<SpuPageVO>> readExcelSpuCode(MultipartFile multipartFile);

	/**
	 * 导入spu简称
	 * @param file
	 * @return
	 */
	String readExcelSpuAbbr(MultipartFile file);
}
