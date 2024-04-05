package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.CpPhoneLibraryDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneLibrary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 引流手机号库
 *
 * @author gmq
 * @date 2023-10-30 17:13:11
 */
public interface CpPhoneLibraryMapper extends BaseMapper<CpPhoneLibrary> {

	/**
	 * 获取引流手机号库列表
	 * @return 引流手机号库列表
	 */
	List<CpPhoneLibrary> list(@Param("dto") CpPhoneLibraryDTO dto);

	/**
	 * 根据引流手机号库id获取引流手机号库
	 *
	 * @param id 引流手机号库id
	 * @return 引流手机号库
	 */
	CpPhoneLibrary getById(@Param("id") Long id);

	CpPhoneLibrary getByPhone(@Param("phone") String phone);

	/**
	 * 保存引流手机号库
	 * @param cpPhoneLibrary 引流手机号库
	 */
	void save(@Param("cpPhoneLibrary") CpPhoneLibrary cpPhoneLibrary);

	/**
	 * 更新引流手机号库
	 * @param cpPhoneLibrary 引流手机号库
	 */
	void update(@Param("cpPhoneLibrary") CpPhoneLibrary cpPhoneLibrary);

	/**
	 * 根据引流手机号库id删除引流手机号库
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
