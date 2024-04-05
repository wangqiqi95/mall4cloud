package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinStoreSubscribe;
import com.mall4j.cloud.biz.vo.WeixinStoreSubscribeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信门店回复内容
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
public interface WeixinStoreSubscribeMapper {

	/**
	 * 获取微信门店回复内容列表
	 * @return 微信门店回复内容列表
	 */
	List<WeixinStoreSubscribe> list(@Param("appId") String appId,@Param("subscribeId") String subscribeId);

	List<WeixinStoreSubscribeVO> getList(@Param("appId") String appId,
										 @Param("storeName")String storeName,
										 @Param("msgType")String msgType,
										 @Param("dataSrc")Integer dataSrc);

	/**
	 * 根据微信门店回复内容id获取微信门店回复内容
	 *
	 * @param id 微信门店回复内容id
	 * @return 微信门店回复内容
	 */
	WeixinStoreSubscribe getById(@Param("id") String id);

	/**
	 * 保存微信门店回复内容
	 * @param weixinStoreSubscribe 微信门店回复内容
	 */
	void save(@Param("weixinStoreSubscribe") WeixinStoreSubscribe weixinStoreSubscribe);

	/**
	 * 更新微信门店回复内容
	 * @param weixinStoreSubscribe 微信门店回复内容
	 */
	void update(@Param("weixinStoreSubscribe") WeixinStoreSubscribe weixinStoreSubscribe);

	/**
	 * 根据微信门店回复内容id删除微信门店回复内容
	 * @param id
	 */
	void deleteById(@Param("updateBy") String updateBy,@Param("id") String id);

	WeixinStoreSubscribeVO getStoreSubscribeByparam(@Param("appId") String appId,
													@Param("storeId")String storeId);
}
