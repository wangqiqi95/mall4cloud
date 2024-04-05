package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * 微信触点二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
public interface WeixinQrcodeTentacleService {

	/**
	 * 分页获取微信触点二维码表列表
	 * @param pageDTO 分页参数
	 * @return 微信触点二维码表列表分页数据
	 */
	PageVO<WeixinQrcodeTentacleVO> page(PageDTO pageDTO, String name, String storeName);

	/**
	 * 根据微信触点二维码表id获取微信触点二维码表
	 *
	 * @param id 微信触点二维码表id
	 * @return 微信触点二维码表
	 */
	WeixinQrcodeTentacle getById(String id);

	/**
	 * 保存微信触点二维码表
	 * @param weixinQrcodeTentacle 微信触点二维码表
	 */
	void save(WeixinQrcodeTentacleDTO weixinQrcodeTentacleDTO);

	/**
	 * 更新微信触点二维码表
	 * @param weixinQrcodeTentacle 微信触点二维码表
	 */
	void update(WeixinQrcodeTentacle weixinQrcodeTentacle);

	/**
	 * 根据微信触点二维码表id删除微信触点二维码表
	 * @param id 微信触点二维码表id
	 */
	void deleteById(String id);

	/**
	 * 发送二维码压缩包至邮箱
	 * @param id
	 * @return
	 */
	ServerResponseEntity<String> sendQrcodeZipToEmail(String id,String receiveEmail);
}
