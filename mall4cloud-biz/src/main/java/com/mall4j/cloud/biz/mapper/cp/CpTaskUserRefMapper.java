package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeDTO;
import com.mall4j.cloud.biz.dto.cp.analyze.CpTagGroupCodeAnalyzeStaffDTO;
import com.mall4j.cloud.biz.model.cp.CpTaskUserRef;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeSendUserVO;
import com.mall4j.cloud.biz.vo.cp.analyze.CpTagGroupCodeStaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发任务客户关联表
 *
 * @author gmq
 * @date 2023-10-30 14:23:04
 */
public interface CpTaskUserRefMapper extends BaseMapper<CpTaskUserRef> {

	/**
	 * 获取群发任务客户关联表列表
	 * @return 群发任务客户关联表列表
	 */
	List<CpTagGroupCodeSendUserVO> list(@Param("dto") CpTagGroupCodeAnalyzeDTO dto);

	List<CpTagGroupCodeStaffVO> countByStaff(@Param("dto") CpTagGroupCodeAnalyzeStaffDTO dto);

	/**
	 * 根据群发任务客户关联表id获取群发任务客户关联表
	 *
	 * @param id 群发任务客户关联表id
	 * @return 群发任务客户关联表
	 */
	CpTaskUserRef getById(@Param("id") Long id);

	CpTaskUserRef getByUserId(@Param("taskId") Long taskId,@Param("qiWeiUserId") String qiWeiUserId);

	/**
	 * 根据群发任务客户关联表id删除群发任务客户关联表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
