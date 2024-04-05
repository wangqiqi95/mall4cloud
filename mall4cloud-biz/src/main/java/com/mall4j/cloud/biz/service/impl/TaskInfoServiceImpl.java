package com.mall4j.cloud.biz.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.service.TaskInfoService;
import com.mall4j.cloud.biz.mapper.TaskInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author wangzhongqi
* @description 针对表【cp_task_info(任务信息表)】的数据库操作Service实现
* @createDate 2024-04-05 12:36:17
*/
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo>
implements TaskInfoService{

}
