package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.GroupCodePageDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCode;
import com.mall4j.cloud.biz.vo.cp.GroupCodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群活码表
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
public interface GroupCodeMapper extends BaseMapper<CpGroupCode> {

	/**
	 * 获取群活码表列表
	 * @return 群活码表列表
	 */
	List<GroupCodeVO> list(@Param("et") GroupCodePageDTO request);

	/**
	 * 根据群活码表id获取群活码表
	 *
	 * @param id 群活码表id
	 * @return 群活码表
	 */
	CpGroupCode getById(@Param("id") Long id);

	/**
	 * 保存群活码表
	 * @param groupCode 群活码表
	 */
	void save(@Param("groupCode") CpGroupCode groupCode);

	/**
	 * 更新群活码表
	 * @param groupCode 群活码表
	 */
	void update(@Param("groupCode") CpGroupCode groupCode);

	/**
	 * 根据群活码表id删除群活码表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
