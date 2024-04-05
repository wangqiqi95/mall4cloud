package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinQrcode;
import com.mall4j.cloud.biz.vo.WeixinQrcodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 微信二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
public interface WeixinQrcodeMapper {

	/**
	 * 获取微信二维码表列表
	 * @return 微信二维码表列表
	 */
	List<WeixinQrcode> list();

	/**
	 * 根据微信二维码表id获取微信二维码表
	 *
	 * @param id 微信二维码表id
	 * @return 微信二维码表
	 */
	WeixinQrcode getById(@Param("id") String id);

	/**
	 * 保存微信二维码表
	 * @param weixinQrcode 微信二维码表
	 */
	void save(@Param("weixinQrcode") WeixinQrcode weixinQrcode);

	/**
	 * 更新微信二维码表
	 * @param weixinQrcode 微信二维码表
	 */
	void update(@Param("weixinQrcode") WeixinQrcode weixinQrcode);

	/**
	 * 根据微信二维码表id删除微信二维码表
	 * @param id
	 */
	void deleteById(@Param("id") String id);

	Integer queryMaxSceneId(@Param("appId") String appId,@Param("actionName") String actionName);

	WeixinQrcodeVO getByTicket(@Param("appId") String appId,@Param("ticket") String ticket);

	WeixinQrcodeVO getByStaff(@Param("staffId") String staffId,@Param("appId") String appId);

	WeixinQrcodeVO getByActionInfo(@Param("actionInfo") String actionInfo,@Param("appId") String appId);

	WeixinQrcodeVO getByContactStr(@Param("contactStr") String contactStr,@Param("appId") String appId);

	List<WeixinQrcodeVO> getList(@Param("actionInfo") String actionInfo,
								 @Param("storeIds") List<Long> storeIds,
								 @Param("startDate") Date startDate,
								 @Param("endDate") Date endDate);

	List<WeixinQrcode> getDownList(@Param("ids") String ids);
}
