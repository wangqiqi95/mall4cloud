package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.ProtectActivitySpuDTO;
import com.mall4j.cloud.product.dto.ProtectActivitySpuPageDTO;
import com.mall4j.cloud.product.dto.UpdateProtectActivitySpuStatusDTO;
import com.mall4j.cloud.product.model.ProtectActivitySpu;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.vo.ProtectActivitySpuVO;
import com.mall4j.cloud.product.vo.SpuExcelImportDataVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2022-06-14 15:17:31
 */
public interface ProtectActivitySpuService extends IService<ProtectActivitySpu> {

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<ProtectActivitySpuVO> page(PageDTO pageDTO, ProtectActivitySpuPageDTO spuPageDTO);

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	ProtectActivitySpuVO getDetailById(Long id);

	/**
	 * 保存
	 * @param protectActivitySpu 
	 */
	void saveTo(ProtectActivitySpuDTO protectActivitySpuDTO);

	void updateTo(ProtectActivitySpuDTO protectActivitySpuDTO);

	void updateStatus(UpdateProtectActivitySpuStatusDTO protectActivitySpuDTO);
	void updateExStatus(UpdateProtectActivitySpuStatusDTO protectActivitySpuDTO);

	/**
	 * 根据id删除
	 * @param id id
	 */
	void deleteByIds(List<Long> ids);

	 ServerResponseEntity<String> importSpus(File file);

	void exportSpus(ProtectActivitySpuPageDTO spuPageDTO,Long downLoadHisId);
}
