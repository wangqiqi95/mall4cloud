package com.mall4j.cloud.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.dto.WeixinTmessageStatusDTO;
import com.mall4j.cloud.biz.vo.TmessageSendVO;
import com.mall4j.cloud.biz.vo.WeixinTmessageAllVO;
import com.mall4j.cloud.biz.vo.WeixinTmessageVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinTmessage;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;

import java.util.List;

/**
 * 微信消息模板
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:17:14
 */
public interface WeixinTmessageService {

	/**
	 * 分页获取微信消息模板列表
	 * @param pageDTO 分页参数
	 * @return 微信消息模板列表分页数据
	 */
	PageVO<WeixinTmessageVO> page(PageDTO pageDTO,String appId,Integer dataSrc);

	/**
	 * 根据微信消息模板id获取微信消息模板
	 *
	 * @param id 微信消息模板id
	 * @return 微信消息模板
	 */
	WeixinTmessage getById(String id);

	/**
	 * 保存微信消息模板
	 * @param weixinTmessage 微信消息模板
	 */
	void save(WeixinTmessage weixinTmessage);

	/**
	 * 更新微信消息模板
	 * @param weixinTmessage 微信消息模板
	 */
	void update(WeixinTmessage weixinTmessage);

	/**
	 * 根据微信消息模板id删除微信消息模板
	 * @param id 微信消息模板id
	 */
	void deleteById(String id);

	ServerResponseEntity<Void> updateStatus(WeixinTmessageStatusDTO weixinTmessageStatusDTO);

	/**
	 * 调用发送模板消息接口
	 * @return
	 */
	public JSONObject sendTemplateMsg(TmessageSendVO tmessageSendVO);

	public JSONObject createSendTemplateMsg(TmessageSendVO tmessageSendVO);

	/**
	 * 获取微信公众号全部消息模板列表
	 * @param pageDTO
	 * @param appId
	 * @return
	 */
	List<WxMpTemplate> getAllPrivateTemplate(String appId);
}
