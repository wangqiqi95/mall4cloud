package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.NotifyConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 应用消息配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface NotifyConfigMapper {

	/**
	 * 获取应用消息配置列表
	 * @param type  消息类型
	 * @return 应用消息配置列表
	 */
	List<NotifyConfig> list(@Param("type") Integer type,@Param("status") Integer status);

	/**
	 * 根据应用消息配置id获取应用消息配置
	 *
	 * @param id 应用消息配置id
	 * @return 应用消息配置
	 */
	NotifyConfig getById(@Param("id") Long id);

	/**
	 * 保存应用消息配置
	 * @param message 应用消息配置
	 */
	void save(@Param("et") NotifyConfig message);

	/**
	 * 更新应用消息配置
	 * @param message 应用消息配置
	 */
	void update(@Param("et") NotifyConfig message);

	/**
	 * 根据应用消息配置id删除应用消息配置
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
