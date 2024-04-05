package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinQrcodePutDTO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcode;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 微信二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
public interface WeixinQrcodeService {

	/**
	 * 分页获取微信二维码表列表
	 * @param pageDTO 分页参数
	 * @return 微信二维码表列表分页数据
	 */
	PageVO<WeixinQrcodeVO> page(PageDTO pageDTO, String actionInfo, String storeId, String startTime, String endTime);

	/**
	 * 根据微信二维码表id获取微信二维码表
	 *
	 * @param id 微信二维码表id
	 * @return 微信二维码表
	 */
	WeixinQrcode getById(String id);

	/**
	 * 保存微信二维码表
	 * @param weixinQrcode 微信二维码表
	 */
	void save(WeixinQrcode weixinQrcode);

	/**
	 * 更新微信二维码表
	 * @param weixinQrcode 微信二维码表
	 */
	void update(WeixinQrcode weixinQrcode);

	/**
	 * 根据微信二维码表id删除微信二维码表
	 * @param id 微信二维码表id
	 */
	void deleteById(String id);

	public Integer getScene(String appId,String actionName);

	void saveWeixinQrcode(WeixinQrcodePutDTO qrcodePutDTO);

	ServerResponseEntity<WeixinQrcode> generateQrcode(WeixinQrcode weixinQrcode);

	void downQrcode(String id, int qrcodeSize, HttpServletRequest request, HttpServletResponse response);

	void downQrcodeAll(String ids, int qrcodeSize, HttpServletRequest request, HttpServletResponse response);

	WeixinQrcodeVO getByTicket(String appId,String ticket);
}
