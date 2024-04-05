package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.biz.dto.WeixinAutoScanDTO;
import com.mall4j.cloud.biz.vo.WeixinAutoKeywordVO;
import com.mall4j.cloud.biz.vo.WeixinAutoScanVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinAutoScan;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 微信扫码回复表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
public interface WeixinAutoScanService {

	/**
	 * 分页获取微信扫码回复表列表
	 * @param pageDTO 分页参数
	 * @return 微信扫码回复表列表分页数据
	 */
	PageVO<WeixinAutoScanVO> page(PageDTO pageDTO,String appId, String name, Integer type, String startTime,String endTime);

	/**
	 * 根据微信扫码回复表id获取微信扫码回复表
	 *
	 * @param id 微信扫码回复表id
	 * @return 微信扫码回复表
	 */
	WeixinAutoScan getById(String id);

	/**
	 * 保存微信扫码回复表
	 * @param weixinAutoScan 微信扫码回复表
	 */
	void save(WeixinAutoScan weixinAutoScan);

	/**
	 * 更新微信扫码回复表
	 * @param weixinAutoScan 微信扫码回复表
	 */
	void update(WeixinAutoScan weixinAutoScan);

	/**
	 * 根据微信扫码回复表id删除微信扫码回复表
	 * @param id 微信扫码回复表id
	 */
	void deleteById(String id);

	ServerResponseEntity<WeixinAutoScanVO> getWeixinAutoScanVO(String id);

	void saveWeixinAutoScan(WeixinAutoScanDTO autoScanDTO);

	List<WeixinAutoScanVO> getReplyListByQrcode(String appId,String qrcodeId);
}
