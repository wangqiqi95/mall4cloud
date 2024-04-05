package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.vo.WeixinMaSubscriptTmessageOptionalValueVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptTmessageOptionalValue;

import java.util.List;

/**
 * 微信小程序订阅模版消息 值
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:45
 */
public interface WeixinMaSubscriptTmessageOptionalValueService {

	/**
	 * 分页获取微信小程序订阅模版消息 值列表
	 * @param pageDTO 分页参数
	 * @return 微信小程序订阅模版消息 值列表分页数据
	 */
	PageVO<WeixinMaSubscriptTmessageOptionalValue> page(PageDTO pageDTO);

	/**
	 * 根据微信小程序订阅模版消息 值id获取微信小程序订阅模版消息 值
	 *
	 * @param id 微信小程序订阅模版消息 值id
	 * @return 微信小程序订阅模版消息 值
	 */
	WeixinMaSubscriptTmessageOptionalValue getById(Long id);

	/**
	 * 保存微信小程序订阅模版消息 值
	 * @param weixinMaSubscriptTmessageOptionalValue 微信小程序订阅模版消息 值
	 */
	void save(WeixinMaSubscriptTmessageOptionalValue weixinMaSubscriptTmessageOptionalValue);

	/**
	 * 更新微信小程序订阅模版消息 值
	 * @param weixinMaSubscriptTmessageOptionalValue 微信小程序订阅模版消息 值
	 */
	void update(WeixinMaSubscriptTmessageOptionalValue weixinMaSubscriptTmessageOptionalValue);

	/**
	 * 根据微信小程序订阅模版消息 值id删除微信小程序订阅模版消息 值
	 * @param id 微信小程序订阅模版消息 值id
	 */
	void deleteById(Long id);

    /**
     * 根据微信小程序场景类型查询可替换值列表
     *
     * @param typeId 业务类型 id
     * @return 微信小程序订阅模版消息 值
     */
    List<WeixinMaSubscriptTmessageOptionalValueVO> getByTypeId(Integer typeId);
}
