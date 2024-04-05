package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.MicroPageBurialPointRecordDTO;
import com.mall4j.cloud.biz.dto.MicroPageBurialPointRecordPageDTO;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.vo.BurialPointStatisticsVO;
import com.mall4j.cloud.biz.vo.MicroPageBurialPointRecordVO;
import com.mall4j.cloud.common.database.dto.TimeBetweenDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * 微页面埋点数据表
 */
public interface MicroPageBurialPointRecordService extends IService<MicroPageBurialPointRecord> {

	/**
	 * 分页获取微页面埋点数据表列表
	 * @param pageDTO 分页参数
	 * @return 微页面埋点数据表列表分页数据
	 */
	PageVO<MicroPageBurialPointRecordVO> page(MicroPageBurialPointRecordPageDTO pageDTO);

	/*
	* 根据时间范围统计埋点数据
	* */
	List<BurialPointStatisticsVO> statisticsBurialPointRecords(Long renovationId,TimeBetweenDTO dto);

	/**
	 * 保存微页面埋点数据表
	 * @param dto 微页面埋点数据
	 */
	void saveBurialPoint(MicroPageBurialPointRecordDTO dto) throws WxErrorException;
}
