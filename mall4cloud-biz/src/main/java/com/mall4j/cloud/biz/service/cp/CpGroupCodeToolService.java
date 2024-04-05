package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeTool;

/**
 * 自动拉群与群活码关联表
 *
 * @author gmq
 * @date 2023-11-06 14:47:45
 */
public interface CpGroupCodeToolService extends IService<CpGroupCodeTool> {


	/**
	 * 根据自动拉群与群活码关联表id获取自动拉群与群活码关联表
	 *
	 * @param id 自动拉群与群活码关联表id
	 * @return 自动拉群与群活码关联表
	 */
	CpGroupCodeTool getByRelGroupId(Long relGroupId,Integer codeFrom);

	CpGroupCodeTool getByGroupId(Long groupId);

}
