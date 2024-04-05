package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.dto.MealActivityPageDTO;
import com.mall4j.cloud.group.model.MealActivity;
import com.mall4j.cloud.group.vo.MealActivityListVO;
import com.mall4j.cloud.group.vo.MealActivityVO;
import com.mall4j.cloud.group.vo.app.MealInfoAppVO;

import java.util.List;

public interface MealActivityService extends IService<MealActivity> {
    MealActivityVO mealDetail(Integer id);

    List<MealActivityVO> mealList();

    MealInfoAppVO mealInfo(Integer id);

    List<MealActivityListVO> mealActivityList(MealActivityPageDTO param);

    MealActivity selectFirstActivity(String shopId);
}
