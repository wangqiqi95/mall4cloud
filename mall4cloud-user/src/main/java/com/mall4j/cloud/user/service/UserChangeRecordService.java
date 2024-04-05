package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.UserChangeRecord;
import java.util.List;

/**
 * 用户变更记录服务
 *
 * @author chaoge
 */
public interface UserChangeRecordService extends IService<UserChangeRecord> {
    /**
     * 获取用户变更记录
     *
     * @param pageNo 翻页参数
     * @param pageSize 翻页参数
     * @param userId 用户id
     * @return 结果
     */
    ServerResponseEntity<CrmPageResult<List<UserChangeRecord>>> getUserChangeRecord(Integer pageNo, Integer pageSize, Long userId);
}
