package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeDTO;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeSelectDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCode;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 自动拉群活码表
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
public interface CpAutoGroupCodeService extends IService<CpAutoGroupCode> {

	/**
	 * 分页获取自动拉群活码表列表
	 * @param pageDTO 分页参数
	 * @return 自动拉群活码表列表分页数据
	 */
	PageVO<CpAutoGroupCodeVO> page(PageDTO pageDTO,CpAutoGroupCodeSelectDTO dto);

	/**
	 * 根据自动拉群活码表id获取自动拉群活码表
	 *
	 * @param id 自动拉群活码表id
	 * @return 自动拉群活码表
	 */
	CpAutoGroupCode getById(Long id);

	CpAutoGroupCodeVO getDetialById(Long id);

	/**
	 * 自动或更新拉群活码表
	 * @param cpAutoGroupCodeDTO 自动拉群活码表
	 */
	void createOrUpate(CpAutoGroupCodeDTO cpAutoGroupCodeDTO);

	/**
	 * 根据自动拉群活码表id删除自动拉群活码表
	 * @param id 自动拉群活码表id
	 */
	void deleteById(Long id);
}
