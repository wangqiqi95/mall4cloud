package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinStoreSubscribeDTO;
import com.mall4j.cloud.biz.dto.WeixinStoreSubscribePutDTO;
import com.mall4j.cloud.biz.vo.WeixinStoreSubscribeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinStoreSubscribe;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 微信门店回复内容
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
public interface WeixinStoreSubscribeService {

	/**
	 * 分页获取微信门店回复内容列表
	 * @param pageDTO 分页参数
	 * @return 微信门店回复内容列表分页数据
	 */
	PageVO<WeixinStoreSubscribeVO> page(PageDTO pageDTO,
										String appId,
										String storeName,
										String msgType);

	/**
	 * 根据微信门店回复内容id获取微信门店回复内容
	 *
	 * @param id 微信门店回复内容id
	 * @return 微信门店回复内容
	 */
	WeixinStoreSubscribe getById(String id);

	/**
	 * 保存微信门店回复内容
	 * @param weixinStoreSubscribe 微信门店回复内容
	 */
	void save(WeixinStoreSubscribe weixinStoreSubscribe);

	/**
	 * 更新微信门店回复内容
	 * @param weixinStoreSubscribe 微信门店回复内容
	 */
	void update(WeixinStoreSubscribe weixinStoreSubscribe);

	/**
	 * 根据微信门店回复内容id删除微信门店回复内容
	 * @param id 微信门店回复内容id
	 */
	void deleteById(String id);

	ServerResponseEntity<Void> saveWeixinStoreSubscribe(WeixinStoreSubscribePutDTO storeSubscribePutDTO);

	ServerResponseEntity<Void> updateWeixinStoreSubscribe(WeixinStoreSubscribeDTO subscribeDTO);

	WeixinStoreSubscribeVO getStoreSubscribeByparam(String appId,String storeId);

	WeixinStoreSubscribeVO getStoreSubscribeById(String id);
}
