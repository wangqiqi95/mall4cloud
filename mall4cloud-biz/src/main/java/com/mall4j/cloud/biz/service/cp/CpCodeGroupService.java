package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpCodeGroup;
import com.mall4j.cloud.biz.vo.cp.CpCodeGroupVO;

import java.util.List;

/**
 * 企微活码分组
 *
 * @author gmq
 * @date 2023-10-23 14:03:45
 */
public interface CpCodeGroupService extends IService<CpCodeGroup> {

	/**
	 * 分页获取企微活码分组列表
	 */
	List<CpCodeGroupVO> list(Integer type,String name);

	/**
	 * 根据企微活码分组id获取企微活码分组
	 *
	 * @param id 企微活码分组id
	 * @return 企微活码分组
	 */
	CpCodeGroup getById(Long id);

	/**
	 * 保存企微活码分组
	 * @param cpCodeGroup 企微活码分组
	 */
	boolean saveTo(CpCodeGroup cpCodeGroup);

	/**
	 * 更新企微活码分组
	 * @param cpCodeGroup 企微活码分组
	 */
	void update(CpCodeGroup cpCodeGroup);

	/**
	 * 根据企微活码分组id删除企微活码分组
	 * @param id 企微活码分组id
	 */
	void deleteById(Long id);
}
