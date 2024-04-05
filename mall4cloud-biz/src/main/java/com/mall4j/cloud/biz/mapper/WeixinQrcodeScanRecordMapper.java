package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinQrcodeScanRecord;
import com.mall4j.cloud.biz.vo.WeixinQrcodeScanRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 微信二维码扫码记录表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
public interface WeixinQrcodeScanRecordMapper {

	/**
	 * 获取微信二维码扫码记录表列表
	 * @return 微信二维码扫码记录表列表
	 */
	List<WeixinQrcodeScanRecord> list();

	List<WeixinQrcodeScanRecordVO> getList(@Param("isScanSubscribe") String isScanSubscribe,
										   @Param("qrcodeId") String qrcodeId,
										   @Param("autoScanId") String autoScanId,
										   @Param("sceneSrc") Integer sceneSrc,
										   @Param("openid") String openid,
										   @Param("startDate") Date startDate,
										   @Param("endDate") Date endDate);

	/**
	 * 根据微信二维码扫码记录表id获取微信二维码扫码记录表
	 *
	 * @param id 微信二维码扫码记录表id
	 * @return 微信二维码扫码记录表
	 */
	WeixinQrcodeScanRecord getById(@Param("id") Long id);

	/**
	 * 保存微信二维码扫码记录表
	 * @param weixinQrcodeScanRecord 微信二维码扫码记录表
	 */
	void save(@Param("weixinQrcodeScanRecord") WeixinQrcodeScanRecord weixinQrcodeScanRecord);

	/**
	 * 更新微信二维码扫码记录表
	 * @param weixinQrcodeScanRecord 微信二维码扫码记录表
	 */
	void update(@Param("weixinQrcodeScanRecord") WeixinQrcodeScanRecord weixinQrcodeScanRecord);

	/**
	 * 根据微信二维码扫码记录表id删除微信二维码扫码记录表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
