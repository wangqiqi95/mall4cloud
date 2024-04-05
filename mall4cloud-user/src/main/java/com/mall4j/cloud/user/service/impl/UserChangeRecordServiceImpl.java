package com.mall4j.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.mapper.UserChangeRecordMapper;
import com.mall4j.cloud.user.model.UserChangeRecord;
import com.mall4j.cloud.user.service.UserChangeRecordService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户变更记录表
 *
 * @author YXF
 * @date 2020-12-08 11:18:04
 */
@Slf4j
@Service
public class UserChangeRecordServiceImpl extends ServiceImpl<UserChangeRecordMapper, UserChangeRecord> implements UserChangeRecordService {

    @Override
    public ServerResponseEntity<CrmPageResult<List<UserChangeRecord>>> getUserChangeRecord(Integer pageNo, Integer pageSize,
                                                                                           Long userId) {
        IPage<UserChangeRecord> page = page(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<UserChangeRecord>().eq(UserChangeRecord::getUserId, userId)
                        .orderByDesc(UserChangeRecord::getId));
        CrmPageResult<List<UserChangeRecord>> data = new CrmPageResult<>();
        data.setTotal_count(page.getTotal());
        data.setJsondata(page.getRecords());
        return ServerResponseEntity.success(data);
    }
}
