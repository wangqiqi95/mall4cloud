package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.dto.MealActivityPageDTO;
import com.mall4j.cloud.group.mapper.MealActivityMapper;
import com.mall4j.cloud.group.model.MealActivity;
import com.mall4j.cloud.group.service.MealActivityService;
import com.mall4j.cloud.group.vo.MealActivityListVO;
import com.mall4j.cloud.group.vo.MealActivityVO;
import com.mall4j.cloud.group.vo.app.MealInfoAppVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MealActivityServiceImpl extends ServiceImpl<MealActivityMapper,MealActivity> implements MealActivityService {
    @Resource
    private MealActivityMapper mealActivityMapper;
    @Override
    public MealActivityVO mealDetail(Integer id) {
        return mealActivityMapper.detail(id);
    }

    @Override
    public List<MealActivityVO> mealList() {
        return mealActivityMapper.getByActivity(new Date());
    }

    @Override
    public MealInfoAppVO mealInfo(Integer id) {
        return mealActivityMapper.info(id);
    }

    @Override
    public List<MealActivityListVO> mealActivityList(MealActivityPageDTO param) {
        return mealActivityMapper.mealActivityList(param);
    }

    @Override
    public MealActivity selectFirstActivity(String shopId) {
        return mealActivityMapper.selectFirstActivity(shopId);
    }
}
