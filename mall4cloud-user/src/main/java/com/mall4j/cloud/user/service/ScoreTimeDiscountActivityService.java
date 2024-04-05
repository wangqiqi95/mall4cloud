package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.ReqScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.dto.ScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivity;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountActivityVO;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountVO;

import java.util.List;

/**
 * 积分限时折扣
 *
 * @author gmq
 * @date 2022-07-11 15:11:39
 */
public interface ScoreTimeDiscountActivityService extends IService<ScoreTimeDiscountActivity> {

	/**
	 * 分页获取积分限时折扣列表
	 * @param pageDTO 分页参数
	 * @return 积分限时折扣列表分页数据
	 */
	PageVO<ScoreTimeDiscountActivityVO> pageData(ReqScoreTimeDiscountActivityDTO dto);

	/**
	 * 根据积分限时折扣id获取积分限时折扣
	 *
	 * @param id 积分限时折扣id
	 * @return 积分限时折扣
	 */
	ScoreTimeDiscountActivityVO getDetailById(Long id);

	/**
	 * 保存积分限时折扣
	 * @param scoreTimeDiscountActivityDTO 积分限时折扣
	 */
	void saveTo(ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO);

	/**
	 * 更新积分限时折扣
	 * @param scoreTimeDiscountActivityDTO 积分限时折扣
	 */
	void updateTo(ScoreTimeDiscountActivityDTO scoreTimeDiscountActivityDTO);

	ServerResponseEntity<Void> openOrClose(Long id, Integer status);

	/**
	 * 根据积分限时折扣id删除积分限时折扣
	 * @param id 积分限时折扣id
	 */
	void deleteToById(Long id);

	/**
	 * 兑换券匹配->限时折扣活动
	 * @param convertIds
	 * @return
	 */
	List<ScoreTimeDiscountVO> getConvertScoreCoupons(List<Long> convertIds);
}
