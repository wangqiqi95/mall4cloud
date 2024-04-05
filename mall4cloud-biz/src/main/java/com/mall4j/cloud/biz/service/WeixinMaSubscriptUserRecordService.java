package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptTmessageVO;
import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.biz.dto.ApplySProductSubscriptTMessageDTO;
import com.mall4j.cloud.biz.dto.ApplySubscriptTMessageDTO;
import com.mall4j.cloud.biz.dto.ApplySubscriptTMessageOpenIdDTO;
import com.mall4j.cloud.biz.dto.ScoreProductSubscriptRecordDTO;
import com.mall4j.cloud.biz.vo.ScoreProductSubscriptRecordVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserRecord;

import java.util.Date;
import java.util.List;

/**
 * 微信小程序用户订阅记录
 *
 * @author FrozenWatermelon
 * @date 2022-03-23 11:10:56
 */
public interface WeixinMaSubscriptUserRecordService {

	/**
	 * 分页获取微信小程序用户订阅记录列表
	 * @param pageDTO 分页参数
	 * @return 微信小程序用户订阅记录列表分页数据
	 */
	PageVO<WeixinMaSubscriptUserRecord> page(PageDTO pageDTO);

	/**
	 * 根据微信小程序用户订阅记录id获取微信小程序用户订阅记录
	 *
	 * @param id 微信小程序用户订阅记录id
	 * @return 微信小程序用户订阅记录
	 */
	WeixinMaSubscriptUserRecord getById(Long id);

	/**
	 * 保存微信小程序用户订阅记录
	 * @param weixinMaSubscriptUserRecord 微信小程序用户订阅记录
	 */
	void save(WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord);

	/**
	 * 更新微信小程序用户订阅记录
	 * @param weixinMaSubscriptUserRecord 微信小程序用户订阅记录
	 */
	void update(WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord);

	/**
	 * 根据微信小程序用户订阅记录id删除微信小程序用户订阅记录
	 * @param id 微信小程序用户订阅记录id
	 */
	void deleteById(Long id);

    /**
     * 用户接受小程序订阅消息
     * @param applySubscriptTMessageDTO
     */
	void applySubscriptTMessage(ApplySubscriptTMessageDTO applySubscriptTMessageDTO);

	/**
	 * 用户接受积分兑礼商品到货通知订阅申请记录
	 * @param tMessageDto
	 */
	void applySProductSubscriptTMessage(ApplySProductSubscriptTMessageDTO tMessageDto);

	/**
	 * 查询用户是否订阅过积分礼品到货通知
	 * @param scoreProductSubscriptRecordDTO
	 * @return
	 */
	List<ScoreProductSubscriptRecordVO> queryScoreProductSubscriptRecord(ScoreProductSubscriptRecordDTO scoreProductSubscriptRecordDTO);

	/**
	 * 用户接受小程序订阅消息
	 * @param dto
	 * @return unionid
	 */
	String applyActivitySubscriptTMessage(ApplySubscriptTMessageOpenIdDTO dto);

	/**
	 * 根据发送类型及开始结束时间查询用户订阅记录
	 * @param sendType
	 * @param beginTime 订阅开始时间
	 * @param endTime 订阅结束时间
	 * @return 用户订阅记录
	 */
	WeixinMaSubscriptTmessageVO queryMaSubscriptUserRecordBySendType(Integer sendType, Date beginTime, Date endTime);
}
