package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.CpAutoGroupCodeSelectDTO;
import com.mall4j.cloud.biz.model.cp.CpAutoGroupCode;
import com.mall4j.cloud.biz.vo.cp.CpAutoGroupCodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自动拉群活码表
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
public interface CpAutoGroupCodeMapper extends BaseMapper<CpAutoGroupCode> {

	/**
	 * 获取自动拉群活码表列表
	 * @return 自动拉群活码表列表
	 */
	List<CpAutoGroupCodeVO> list(@Param("dto")CpAutoGroupCodeSelectDTO dto);

	/**
	 * 根据自动拉群活码表id获取自动拉群活码表
	 *
	 * @param id 自动拉群活码表id
	 * @return 自动拉群活码表
	 */
	CpAutoGroupCode getById(@Param("id") Long id);

	/**
	 * 根据自动拉群活码表id删除自动拉群活码表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
