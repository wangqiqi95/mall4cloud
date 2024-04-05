package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpCodeGroup;
import com.mall4j.cloud.biz.vo.cp.CpCodeGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企微活码分组
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
public interface CpCodeGroupMapper extends BaseMapper<CpCodeGroup> {

	/**
	 * 获取企微活码分组列表
	 * @return 企微活码分组列表
	 */
	List<CpCodeGroupVO> list(@Param("type") Integer type,@Param("name") String name);

	/**
	 * 根据企微活码分组id获取企微活码分组
	 *
	 * @param id 企微活码分组id
	 * @return 企微活码分组
	 */
	CpCodeGroup getById(@Param("id") Long id);

	/**
	 * 保存企微活码分组
	 * @param cpCodeGroup 企微活码分组
	 */
	void save(@Param("cpCodeGroup") CpCodeGroup cpCodeGroup);

	/**
	 * 更新企微活码分组
	 * @param cpCodeGroup 企微活码分组
	 */
	void update(@Param("cpCodeGroup") CpCodeGroup cpCodeGroup);

	/**
	 * 根据企微活码分组id删除企微活码分组
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
