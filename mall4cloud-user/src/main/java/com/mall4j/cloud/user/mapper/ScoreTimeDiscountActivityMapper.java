package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.ReqScoreTimeDiscountActivityDTO;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivity;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分限时折扣
 *
 * @author gmq
 * @date 2022-07-11 15:11:39
 */
public interface ScoreTimeDiscountActivityMapper extends BaseMapper<ScoreTimeDiscountActivity> {

	/**
	 * 获取积分限时折扣列表
	 * @return 积分限时折扣列表
	 */
	List<ScoreTimeDiscountActivity> list();

	List<ScoreTimeDiscountActivityVO> getList(@Param("dto") ReqScoreTimeDiscountActivityDTO dto);

	/**
	 * 根据积分限时折扣id获取积分限时折扣
	 *
	 * @param id 积分限时折扣id
	 * @return 积分限时折扣
	 */
	ScoreTimeDiscountActivityVO getDetailById(@Param("id") Long id);

	boolean deleteToById(@Param("id") Long id);

	boolean saveTo(@Param("scoreTimeDiscountActivity") ScoreTimeDiscountActivity scoreTimeDiscountActivity);
}
