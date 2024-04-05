package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.UserActionQueryDTO;
import com.mall4j.cloud.user.model.UserAction;
import com.mall4j.cloud.user.service.UserActionService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户行为足迹表
 *
 * @author gww
 * @date 2022-02-18 16:29:23
 */
public interface UserActionMapper extends BaseMapper<UserAction> {

	/**
	 * 获取用户行为足迹表列表
	 *
	 * @param userActionQueryDTO
	 *
	 * @return 用户行为足迹表列表
	 */
	List<UserActionService> list(UserActionQueryDTO userActionQueryDTO);

	void saveBatch(@Param("userActionList") List<UserAction> userActionList);

}
