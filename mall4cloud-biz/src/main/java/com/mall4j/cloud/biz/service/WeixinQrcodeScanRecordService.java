package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.api.biz.vo.WeixinQrcodeScanRecordLogVO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeScanRecordDTO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeScanRecordVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcodeScanRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 微信二维码扫码记录表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
public interface WeixinQrcodeScanRecordService {

	/**
	 * 分页获取微信二维码扫码记录表列表
	 * @param pageDTO 分页参数
	 * @return 微信二维码扫码记录表列表分页数据
	 */
	PageVO<WeixinQrcodeScanRecord> page(PageDTO pageDTO);
	PageVO<WeixinQrcodeScanRecordVO> pageList(PageDTO pageDTO,
											  String isScanSubscribe,
											  String sceneId,
											  String autoScanId,
											  Integer sceneSrc,
											  String openid,
											  String startTime,
											  String endTime);
	List<WeixinQrcodeScanRecordVO> exportList(
											  String isScanSubscribe,
											  String sceneId,
											  String autoScanId,
											  Integer sceneSrc,
											  String openid,
											  String startTime,
											  String endTime);

	/**
	 * 根据微信二维码扫码记录表id获取微信二维码扫码记录表
	 *
	 * @param id 微信二维码扫码记录表id
	 * @return 微信二维码扫码记录表
	 */
	WeixinQrcodeScanRecord getById(Long id);

	/**
	 * 保存微信二维码扫码记录表
	 * @param weixinQrcodeScanRecord 微信二维码扫码记录表
	 */
	void save(WeixinQrcodeScanRecord weixinQrcodeScanRecord);

	/**
	 * 更新微信二维码扫码记录表
	 * @param weixinQrcodeScanRecord 微信二维码扫码记录表
	 */
	void update(WeixinQrcodeScanRecord weixinQrcodeScanRecord);

	/**
	 * 根据微信二维码扫码记录表id删除微信二维码扫码记录表
	 * @param id 微信二维码扫码记录表id
	 */
	void deleteById(Long id);

	void saveQrcodeScanRecord (WeixinQrcodeScanRecordDTO scanRecordDTO);

	/**
	 * 导出扫码自动回复(数据详情)
	 * @param isScanSubscribe
	 * @param sceneId
	 * @param sceneSrc
	 * @param openid
	 * @param startTime
	 * @param endTime
	 */
	List<WeixinQrcodeScanRecordLogVO> soldScanRecs(
			String isScanSubscribe,
			String sceneId,
			String autoScanId,
			Integer sceneSrc,
			String openid,
			String startTime,
			String endTime,
			Long downLoadHisId,
			HttpServletResponse response);
}
