package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.api.biz.vo.WeixinMaSubscriptUserRecordVO;
import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 微信小程序用户订阅记录
 *
 * @author FrozenWatermelon
 * @date 2022-03-23 11:10:56
 */
public interface WeixinMaSubscriptUserRecordMapper {

	/**
	 * 获取微信小程序用户订阅记录列表
	 * @return 微信小程序用户订阅记录列表
	 */
	List<WeixinMaSubscriptUserRecord> list();

	/**
	 * 根据微信小程序用户订阅记录id获取微信小程序用户订阅记录
	 *
	 * @param id 微信小程序用户订阅记录id
	 * @return 微信小程序用户订阅记录
	 */
	WeixinMaSubscriptUserRecord getById(@Param("id") Long id);

	/**
	 * 保存微信小程序用户订阅记录
	 * @param weixinMaSubscriptUserRecord 微信小程序用户订阅记录
	 */
	void save(@Param("weixinMaSubscriptUserRecord") WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord);

	/**
	 * 更新微信小程序用户订阅记录
	 * @param weixinMaSubscriptUserRecord 微信小程序用户订阅记录
	 */
	void update(@Param("weixinMaSubscriptUserRecord") WeixinMaSubscriptUserRecord weixinMaSubscriptUserRecord);

	/**
	 * 根据微信小程序用户订阅记录id删除微信小程序用户订阅记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);


    List<WeixinMaSubscriptUserRecordVO> getByTemplateId(@Param("templateId") String templateId);

    List<WeixinMaSubscriptUserRecordVO> getByTemplateIdAndBusIds(@Param("templateId") String templateId,@Param("lists") List<String> lists);

    Integer sendMessage(@Param("id") Long id);

	Integer sendMessageUpdateStatus(@Param("id") Long id);

	List<WeixinMaSubscriptUserRecordVO> getUserRecordByUserRecordIds(@Param("userRecordIds") List<Long> userRecordIds);

	List<WeixinMaSubscriptUserRecordVO> getByTemplateIdAndBusId(@Param("templateId") String templateId, @Param("bussinessId") String bussinessId);

	Integer cancelUserSubscriptRecord(@Param("userId") Long userId,@Param("sendType") Integer sendType,@Param("bussinessId") Long bussinessId);

	List<WeixinMaSubscriptUserRecordVO> getByTemplateIdAndBegTimeAndEndTime(@Param("templateId") String templateId, @Param("beginTime") Date beginTime,@Param("endTime") Date endTime);
}
