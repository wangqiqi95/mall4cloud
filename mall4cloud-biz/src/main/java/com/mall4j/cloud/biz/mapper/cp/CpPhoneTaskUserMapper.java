package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.CpSelectPhoneTaskUserDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskUser;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserCountVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 引流手机号任务关联客户
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
public interface CpPhoneTaskUserMapper extends BaseMapper<CpPhoneTaskUser> {

	/**
	 * 获取引流手机号任务关联客户列表
	 * @return 引流手机号任务关联客户列表
	 */
	List<CpPhoneTaskUserVO> list(@Param("dto") CpSelectPhoneTaskUserDTO dto);

	/**
	 * 查询需要回收的手机号
	 * @param taskId
	 * @return
	 */
	List<CpPhoneTaskUser> selectRecycleList(@Param("taskId") Long taskId);

	/**
	 * 根据引流手机号任务关联客户id获取引流手机号任务关联客户
	 *
	 * @param id 引流手机号任务关联客户id
	 * @return 引流手机号任务关联客户
	 */
	CpPhoneTaskUser getById(@Param("id") Long id);

	/**
	 * 根据手机号库ID获取任务记录
	 * @param phoneUserId
	 * @return
	 */
	CpPhoneTaskUser selectSuccessByPhoneUserId(@Param("phoneUserId") Long phoneUserId,@Param("staffId") Long staffId);

	/**
	 * 根据引流手机号任务关联客户id删除引流手机号任务关联客户
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<CpPhoneTaskUserCountVO> selectCountByTaskId(@Param("taskIds") List<Long> taskIds);
}
