package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivity;
import com.mall4j.cloud.user.model.ScoreTimeDiscountActivityItem;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountActivityItemVO;
import com.mall4j.cloud.user.vo.ScoreTimeDiscountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分限时折扣兑换券
 *
 * @author gmq
 * @date 2022-07-11 15:12:37
 */
public interface ScoreTimeDiscountActivityItemMapper extends BaseMapper<ScoreTimeDiscountActivityItem> {

	/**
	 * 获取积分限时折扣兑换券列表
	 * @return 积分限时折扣兑换券列表
	 */
	List<ScoreTimeDiscountActivityItem> list();

	List<ScoreTimeDiscountActivityItemVO> getItemListByActivityId(@Param("activityId") String activityId);

	List<ScoreTimeDiscountVO> getContentItems(@Param("converIds") List<Long> converIds);

	/**
	 * 根据积分限时折扣兑换券id获取积分限时折扣兑换券
	 *
	 * @param id 积分限时折扣兑换券id
	 * @return 积分限时折扣兑换券
	 */
	ScoreTimeDiscountActivityItem getById(@Param("id") Long id);
}
