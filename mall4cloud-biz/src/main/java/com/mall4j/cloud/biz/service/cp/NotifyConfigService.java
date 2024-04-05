package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.biz.constant.cp.NotityTypeEunm;
import com.mall4j.cloud.biz.model.cp.NotifyConfig;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;


/**
 * 应用消息配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface NotifyConfigService {

	/**
	 * 分页获取应用消息配置列表
	 * @param pageDTO 分页参数
	 * @return 应用消息配置列表分页数据
	 */
	PageVO<NotifyConfig> page(PageDTO pageDTO);

	/**
	 * 根据应用消息配置id获取应用消息配置
	 *
	 * @param id 应用消息配置id
	 * @return 应用消息配置
	 */
	NotifyConfig getById(Long id);

	/**
	 * 保存应用消息配置
	 * @param message 应用消息配置
	 */
	void save(NotifyConfig message);

	/**
	 * 更新应用消息配置
	 * @param message 应用消息配置
	 */
	void update(NotifyConfig message);

	/**
	 * 根据应用消息配置id删除应用消息配置
	 * @param id 应用消息配置id
	 */
	void deleteById(Long id);

	/**
	 * 根据类型获取配置消息
	 * @param type 类型
	 * @return
	 */
	NotifyConfig getNotifyByType(NotityTypeEunm type);

	List<NotifyConfig> list ();
}
