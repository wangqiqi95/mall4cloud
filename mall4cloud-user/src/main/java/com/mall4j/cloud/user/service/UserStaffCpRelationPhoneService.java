package com.mall4j.cloud.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.model.UserStaffCpRelationPhone;

import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2023-11-28 14:35:39
 */
public interface UserStaffCpRelationPhoneService extends IService<UserStaffCpRelationPhone> {


	void saveTo(Long relationId,String userId,Long staffId,String staffUserId,String mobiles,Integer status);

	List<UserStaffCpRelationPhone> getListByPhone(List<String> phone);

	/**
	 * 根据id删除
	 * @param userId
	 */
	void deleteById(String userId,Integer status);
}
