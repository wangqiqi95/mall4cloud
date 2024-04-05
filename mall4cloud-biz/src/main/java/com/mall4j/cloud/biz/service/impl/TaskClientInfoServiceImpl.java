package com.mall4j.cloud.biz.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import com.mall4j.cloud.biz.service.TaskClientInfoService;
import com.mall4j.cloud.biz.mapper.TaskClientInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author wangzhongqi
* @description 针对表【cp_task_client_info(任务客户表)】的数据库操作Service实现
* @createDate 2024-04-05 12:36:17
*/
@Service
public class TaskClientInfoServiceImpl extends ServiceImpl<TaskClientInfoMapper, TaskClientInfo>
implements TaskClientInfoService{

}
