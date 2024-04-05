package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.MealActivityPageDTO;
import com.mall4j.cloud.group.model.MealActivity;
import com.mall4j.cloud.group.vo.MealActivityListVO;
import com.mall4j.cloud.group.vo.MealActivityVO;
import com.mall4j.cloud.group.vo.app.MealInfoAppVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MealActivityMapper extends BaseMapper<MealActivity> {
    MealActivityVO detail(@Param("id") Integer id);

    List<MealActivityVO> getByActivity(@Param("endTime")Date endTime);

    MealInfoAppVO info(Integer id);

    List<MealActivityListVO> mealActivityList(MealActivityPageDTO param);

    MealActivity selectFirstActivity(@Param("shopId") String shopId);
}
