package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表
 *
 * @author FrozenWatermelon
 * @date 2022-04-06 14:24:19
 */
public interface UserMapper {

	/**
	 * 获取用户表列表
	 * @return 用户表列表
	 */
	List<User> list();

	/**
	 * 根据用户表id获取用户表
	 *
	 * @param userId 用户表id
	 * @return 用户表
	 */
	User getByUserId(@Param("userId") Long userId);

    User getByVipCode(@Param("vipCode") String vipCode);

    User getByVipCode2(@Param("vipCode") String vipCode);

    User getByVipCode3(@Param("vipCode") String vipCode);

    User getByunionId2(@Param("unionId") String unionId);

    User getByunionIdAll(@Param("unionId") String unionId);


    int updateVipCodeById(@Param("vipCode")String vipCode,@Param("userId")Long userId);

	/**
	 * 保存用户表
	 * @param user 用户表
	 */
	void save(@Param("user") User user);

    /**
     * 批量保存
     * @param users
     */
	void batchSave(@Param("users") List<User> users);

    void batchSave2(@Param("users") List<User> users);

    void batchSave3(@Param("users") List<User> users);

	/**
	 * 更新用户表
	 * @param user 用户表
	 */
	void update(@Param("user") User user);

	/**
	 * 根据用户表id删除用户表
	 * @param userId
	 */
	void deleteById(@Param("userId") Long userId);
}
