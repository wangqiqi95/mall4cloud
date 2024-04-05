package com.service.impl;

import com.mall4j.cloud.biz.mapper.TaskShoppingGuideMapper;
import com.service.TaskShoppingGuideService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * 导购任务关联表(TaskShoppingGuide)表服务实现类
 *
 * @author makejava
 * @since 2024-04-01 14:15:53
 */
@Service("taskShoppingGuideService")
public class TaskShoppingGuideServiceImpl implements TaskShoppingGuideService {
    @Resource
    private TaskShoppingGuideMapper taskShoppingGuideMapper;


}
