package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.util.ArrayUtil;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.biz.constant.cp.QiweiUserStatus;
import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyPlusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-10 17:28
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxCpUserNotifyServicePlusImpl implements WxCpUserNotifyPlusService {
//    private  final OnsMQTemplate staffSyncTemplate;
    private final StaffFeignClient staffFeignClient;

    @Override
    public void create(String userId, String mobile, String status,String createTime,String avater,Long[] departIds) {
        log.info("----user create -------userId：【"+userId+"】  mobile:【"+mobile+"】 status:【"+status+"】");
        StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
        staffSyncDTO.setQiweiUserId(userId);
        staffSyncDTO.setQiweiUserStatus(QiweiUserStatus.ALIVE.getCode());
        staffSyncDTO.setCreateTime(createTime);
        if(StringUtils.isNotEmpty(status)) {
            staffSyncDTO.setQiweiUserStatus(Integer.parseInt(status));
        }
        if(ArrayUtil.isNotEmpty(departIds)){
            List<String> orgIds = Arrays.stream(departIds).map(String::valueOf).collect(Collectors.toList());
            staffSyncDTO.setOrgIds(orgIds);
        }
        staffSyncDTO.setAvater(avater);
        staffSyncDTO.setMobile(mobile);
//        staffFeignClient.staffSyncMessage(staffSyncDTO);
//        staffSyncTemplate.syncSend(staffSyncDTO);

    }

    @Override
    public void update(String userId, String newUserId, String mobile, String status,String createTime,String avater,Long[] departIds) {
        log.info("----user update -------userId：【"+userId+"】 newUserId:【"+newUserId+"】 mobile:【"+mobile+"】 status:【"+status+"】");
        StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
        staffSyncDTO.setQiweiUserId(userId);
        staffSyncDTO.setMobile(mobile);
        staffSyncDTO.setCreateTime(createTime);
        if(StringUtils.isNotEmpty(newUserId)) {
            staffSyncDTO.setQiweiUserId(newUserId);
        }
        staffSyncDTO.setAvater(avater);
        if(StringUtils.isNotEmpty(status)) {
            staffSyncDTO.setQiweiUserStatus(Integer.parseInt(status));
        }
        if(ArrayUtil.isNotEmpty(departIds)){
            List<String> orgIds = Arrays.stream(departIds).map(String::valueOf).collect(Collectors.toList());
            staffSyncDTO.setOrgIds(orgIds);
        }
        staffFeignClient.staffSyncMessage(staffSyncDTO);
//        staffSyncTemplate.syncSend(staffSyncDTO);
    }


    @Override
    public void delete(String userId,String createTime) {
        log.info("----user delete -------userId：【"+userId+"】 createTime："+createTime);
        StaffSyncDTO staffSyncDTO = new StaffSyncDTO();
        staffSyncDTO.setCreateTime(createTime);
        staffSyncDTO.setQiweiUserId(userId);
        staffSyncDTO.setQiweiUserStatus(QiweiUserStatus.DEL.getCode());
        staffFeignClient.staffSyncMessage(staffSyncDTO);
//        staffSyncTemplate.syncSend(staffSyncDTO);
    }

    @Override
    public void tranSferFail(WxCpXmlMessage wxMessage, String failReason) {

    }

    /**
     * 新增部门
     * @param wxMessage
     * @param wxCpDepart
     */
    @Override
    public void addDepart(WxCpXmlMessage wxMessage, WxCpDepart wxCpDepart) {

    }

    /**
     * 更新部门
     * @param wxMessage
     * @param wxCpDepart
     */
    @Override
    public void updateDepart(WxCpXmlMessage wxMessage, WxCpDepart wxCpDepart) {

    }

    /**
     * 删除部门
     * @param wxMessage
     * @param wxCpDepart
     */
    @Override
    public void deleteDepart(WxCpXmlMessage wxMessage, WxCpDepart wxCpDepart) {

    }

}
