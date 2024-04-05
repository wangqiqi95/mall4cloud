package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyService;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-10 17:28
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxCpUserNotifyServiceImpl implements WxCpUserNotifyService {
    private  final OnsMQTemplate staffSyncTemplate;

    @Override
    public void create(String userId, String mobile, String status) {
        log.info("----user create -------userId：【"+userId+"】  mobile:【"+mobile+"】 status:【"+status+"】");
        StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
        staffSyncDTO.setQiweiUserId(userId);
        staffSyncDTO.setQiweiUserStatus(QiweiUserStatus.ALIVE.getCode());
        if(StringUtils.isNotEmpty(status)) {
            staffSyncDTO.setQiweiUserStatus(Integer.parseInt(status));
        }
        staffSyncDTO.setMobile(mobile);
        staffSyncTemplate.syncSend(staffSyncDTO);

    }

    @Override
    public void update(String userId, String newUserId, String mobile, String status) {
        log.info("----user update -------userId：【"+userId+"】 newUserId:【"+newUserId+"】 mobile:【"+mobile+"】 status:【"+status+"】");
        StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
        staffSyncDTO.setQiweiUserId(userId);
        staffSyncDTO.setMobile(mobile);
        if(StringUtils.isNotEmpty(newUserId)) {
            staffSyncDTO.setQiweiUserId(newUserId);
        }
        //staffSyncDTO.setQiweiUserStatus(Integer.parseInt(status));
        staffSyncTemplate.syncSend(staffSyncDTO);
    }

    @Override
    public void delete(String userId) {
        log.info("----user delete -------userId：【"+userId+"】 ");
        StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
        staffSyncDTO.setQiweiUserId(userId);
        staffSyncDTO.setQiweiUserStatus(QiweiUserStatus.DEL.getCode());
        staffSyncTemplate.syncSend(staffSyncDTO);
    }

    @Override
    public void tranSferFail(String userId, String externalUserId, String failReason) {

    }

}
