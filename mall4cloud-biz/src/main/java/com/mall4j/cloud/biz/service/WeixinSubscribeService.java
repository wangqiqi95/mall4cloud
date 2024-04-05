package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinSubscribePutDTO;
import com.mall4j.cloud.biz.vo.WeixinSubscribeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinSubscribe;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * 微信关注回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:59
 */
public interface WeixinSubscribeService {

	/**
	 * 分页获取微信关注回复列表
	 * @param pageDTO 分页参数
	 * @return 微信关注回复列表分页数据
	 */
	PageVO<WeixinSubscribe> page(PageDTO pageDTO);

	/**
	 * 根据微信关注回复id获取微信关注回复
	 *
	 * @param id 微信关注回复id
	 * @return 微信关注回复
	 */
	WeixinSubscribe getById(Long id);

	/**
	 * 保存微信关注回复
	 * @param weixinSubscribe 微信关注回复
	 */
	void save(WeixinSubscribe weixinSubscribe);

	/**
	 * 更新微信关注回复
	 * @param weixinSubscribe 微信关注回复
	 */
	void update(WeixinSubscribe weixinSubscribe);

	/**
	 * 根据微信关注回复id删除微信关注回复
	 * @param id 微信关注回复id
	 */
	void deleteById(Long id);

	WeixinSubscribe getWeixinSubscribe(String appId, Integer replyType);

	ServerResponseEntity<WeixinSubscribeVO> getWeixinSubscribeVO(String appId, Integer replyType, String msgType);

	void saveWeixinSubscribe(WeixinSubscribePutDTO subscribeDTO);
}
