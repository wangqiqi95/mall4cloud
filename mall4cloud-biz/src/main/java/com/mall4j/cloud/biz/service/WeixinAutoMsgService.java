package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinAutoMsgPutDTO;
import com.mall4j.cloud.biz.dto.WeixinSubscribePutDTO;
import com.mall4j.cloud.biz.vo.WeixinAutoMsgVO;
import com.mall4j.cloud.biz.vo.WeixinSubscribeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoMsg;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 微信消息自动回复
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:52:24
 */
public interface WeixinAutoMsgService {

	/**
	 * 分页获取微信消息自动回复列表
	 * @param pageDTO 分页参数
	 * @return 微信消息自动回复列表分页数据
	 */
	PageVO<WeixinAutoMsg> page(PageDTO pageDTO);

	/**
	 * 根据微信消息自动回复id获取微信消息自动回复
	 *
	 * @param id 微信消息自动回复id
	 * @return 微信消息自动回复
	 */
	WeixinAutoMsg getById(Long id);

	/**
	 * 保存微信消息自动回复
	 * @param weixinAutoMsg 微信消息自动回复
	 */
	void save(WeixinAutoMsg weixinAutoMsg);

	/**
	 * 更新微信消息自动回复
	 * @param weixinAutoMsg 微信消息自动回复
	 */
	void update(WeixinAutoMsg weixinAutoMsg);

	/**
	 * 根据微信消息自动回复id删除微信消息自动回复
	 * @param id 微信消息自动回复id
	 */
	void deleteById(Long id);

	WeixinAutoMsg getWeixinAutoMsg(String appId);

	ServerResponseEntity<WeixinAutoMsgVO> getWeixinAutoMsgVO(String appId, String msgType);

	void saveWeixinAutoMsg(WeixinAutoMsgPutDTO autoMsgPutDTO);
}
