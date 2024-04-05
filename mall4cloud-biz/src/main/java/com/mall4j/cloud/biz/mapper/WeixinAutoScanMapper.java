package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinAutoScan;
import com.mall4j.cloud.biz.vo.WeixinAutoScanVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 微信扫码回复表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
public interface WeixinAutoScanMapper {

	/**
	 * 获取微信扫码回复表列表
	 * @return 微信扫码回复表列表
	 */
	List<WeixinAutoScan> list();

	List<WeixinAutoScanVO> getList(@Param("appId") String appId,
								   @Param("name") String name,
								   @Param("type") Integer type,
								   @Param("startDate") Date startDate,
								   @Param("endDate") Date endDate);

	/**
	 * 根据微信扫码回复表id获取微信扫码回复表
	 *
	 * @param id 微信扫码回复表id
	 * @return 微信扫码回复表
	 */
	WeixinAutoScan getById(@Param("id") String id);

	/**
	 * 保存微信扫码回复表
	 * @param weixinAutoScan 微信扫码回复表
	 */
	void save(@Param("weixinAutoScan") WeixinAutoScan weixinAutoScan);

	/**
	 * 更新微信扫码回复表
	 * @param weixinAutoScan 微信扫码回复表
	 */
	void update(@Param("weixinAutoScan") WeixinAutoScan weixinAutoScan);

	/**
	 * 根据微信扫码回复表id删除微信扫码回复表
	 * @param id
	 */
	void deleteById(@Param("id") String id);

	List<WeixinAutoScanVO> getReplyListByQrcode(@Param("appId") String appId,
								   @Param("qrcodeId") String qrcodeId);
}
