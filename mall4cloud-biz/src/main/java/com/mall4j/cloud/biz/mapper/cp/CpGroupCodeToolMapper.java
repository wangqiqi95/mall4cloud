package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeTool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自动拉群与群活码关联表
 *
 * @author gmq
 * @date 2023-11-06 14:47:45
 */
public interface CpGroupCodeToolMapper extends BaseMapper<CpGroupCodeTool> {


	/**
	 * 根据自动拉群与群活码关联表id获取自动拉群与群活码关联表
	 *
	 * @param id 自动拉群与群活码关联表id
	 * @return 自动拉群与群活码关联表
	 */
	CpGroupCodeTool getByRelGroupId(@Param("relGroupId") Long relGroupId,@Param("codeFrom") Integer codeFrom);

	CpGroupCodeTool getByGroupId(@Param("groupId") Long groupId);

	List<CpGroupCodeTool> getByGroupIds(@Param("groupIds") List<String> groupIds);

}
