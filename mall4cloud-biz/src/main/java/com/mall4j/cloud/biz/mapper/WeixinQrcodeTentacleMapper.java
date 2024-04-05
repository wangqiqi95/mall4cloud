package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信触点二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
public interface WeixinQrcodeTentacleMapper {

	/**
	 * 获取微信触点二维码表列表
	 * @return 微信触点二维码表列表
	 */
	List<WeixinQrcodeTentacle> list();

	List<WeixinQrcodeTentacleVO> getList(@Param("name") String name, @Param("storeId") String storeId);

	/**
	 * 根据微信触点二维码表id获取微信触点二维码表
	 *
	 * @param id 微信触点二维码表id
	 * @return 微信触点二维码表
	 */
	WeixinQrcodeTentacle getById(@Param("id") String id);

	/**
	 * 保存微信触点二维码表
	 * @param weixinQrcodeTentacle 微信触点二维码表
	 */
	void save(@Param("weixinQrcodeTentacle") WeixinQrcodeTentacle weixinQrcodeTentacle);

	/**
	 * 更新微信触点二维码表
	 * @param weixinQrcodeTentacle 微信触点二维码表
	 */
	void update(@Param("weixinQrcodeTentacle") WeixinQrcodeTentacle weixinQrcodeTentacle);

	/**
	 * 根据微信触点二维码表id删除微信触点二维码表
	 * @param id
	 */
	void deleteById(@Param("id") String id);
}
