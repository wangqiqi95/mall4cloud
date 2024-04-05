package com.mall4j.cloud.platform.listener;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeRefPDTO;
import com.mall4j.cloud.api.biz.feign.StaffCodeFeignClient;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.platform.mapper.TzStoreFilterMapper;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.model.TzStore;
import com.mall4j.cloud.platform.model.TzStoreFilter;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.TzStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.STAFF_SYNC_TOPIC,consumerGroup = "GID_"+RocketMqConstant.STAFF_SYNC_TOPIC)
public class StaffSyncConsumer implements RocketMQListener<StaffSyncDTO> {

    @Autowired
    private StaffService staffService;
    @Autowired
    private TzStoreService tzStoreService;
    @Autowired
    private SegmentFeignClient segmentFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private StaffCodeFeignClient staffCodeFeignClient;
    @Autowired
    private TzStoreFilterMapper tzStoreFilterMapper;

    @Override
    public void onMessage(StaffSyncDTO staffSyncDTO) {
        log.info("staff sync param : {} ", JSONObject.toJSONString(staffSyncDTO));

        //执行门店是否过滤员工同步
        if (!StringUtils.isEmpty(staffSyncDTO.getStoreCode())){
             TzStoreFilter tzStoreFilter = tzStoreFilterMapper.getBystoreCode(staffSyncDTO.getStoreCode());
             if (tzStoreFilter!= null && tzStoreFilter.getIsFilterStaff() == 1){
                 return;
             }
        }

        if (StringUtils.isEmpty(staffSyncDTO.getMobile()) && StringUtils.isEmpty(staffSyncDTO.getQiweiUserId())) {
            log.info("同步失败，手机号和企业微信id传其一");
            return;
        }

        StaffVO staffVO;
        if (StringUtils.isNotEmpty(staffSyncDTO.getStaffCode())) {
            staffVO = staffService.getByStaffCode(staffSyncDTO.getStaffCode());
        }else if (StringUtils.isNotEmpty(staffSyncDTO.getMobile())) {
            staffVO = staffService.getByMobile(staffSyncDTO.getMobile());
        } else {
            staffVO = staffService.getByQiWeiUserId(staffSyncDTO.getQiweiUserId());
        }

        // 默认官店
        Long storeId = 1L;
        if (Objects.isNull(staffVO)) {
            if (StringUtils.isNotBlank(staffSyncDTO.getQiweiUserId())) {
                log.info("员工数据不存在，企微不进行同步");
                return;
            }
            Staff staff = new Staff();
            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_STAFF_USER);
            if (!segmentIdResponse.isSuccess()) {
                throw new LuckException(segmentIdResponse.getMsg());
            }
            Long staffId = segmentIdResponse.getData();
            staff.setId(staffId);
            staff.setStaffName(staffSyncDTO.getName());
            staff.setMobile(staffSyncDTO.getMobile());
            staff.setStaffCode(staffSyncDTO.getStaffCode());
            staff.setStaffNo(staffSyncDTO.getStaffNo());
            staff.setStoreId(storeId);
            staff.setRoleType(staffSyncDTO.getRoleType());
            staff.setCreateTime(new Date());
            staff.setUpdateTime(new Date());
            staff.setStatus(staffSyncDTO.getIsLeave().equalsIgnoreCase("Y") ? 1 : 0);
            staffService.add(staff);
        } else {
            Staff staff = new Staff();
            staff.setId(staffVO.getId());
            staff.setMobile(staffSyncDTO.getMobile());
            staff.setStaffName(staffSyncDTO.getName());
            staff.setStaffCode(staffSyncDTO.getStaffCode());
            staff.setStaffNo(staffSyncDTO.getStaffNo());
            staff.setQiWeiUserId(staffSyncDTO.getQiweiUserId());
            staff.setQiWeiUserStatus(staffSyncDTO.getQiweiUserStatus());
            if(staff.getQiWeiUserStatus()==5&& CharSequenceUtil.isNotEmpty(staffSyncDTO.getCreateTime())){//离职
                staff.setDimissionTime(DateUtil.formatDateWX(staffSyncDTO.getCreateTime()));
            }
            if (StringUtils.isNotBlank(staffSyncDTO.getIsLeave())) {
                staff.setStatus(staffSyncDTO.getIsLeave().equalsIgnoreCase("Y") ? 1 : 0);
            }
            if (StringUtils.isNotBlank(staffSyncDTO.getStoreCode())) {
                List<String> storeCodeList = new ArrayList<>();
                storeCodeList.add(staffSyncDTO.getStoreCode());
                List<TzStore> tzStoreList = tzStoreService.listByStoreCode(storeCodeList);
                if (CollectionUtils.isEmpty(tzStoreList)) {
                    log.info("同步失败，门店不存在");
                    return;
                }
                storeId = tzStoreList.get(0).getStoreId();
            }
            staff.setStoreId(storeId);
            staff.setRoleType(staffSyncDTO.getRoleType());
            staff.setUpdateTime(new Date());
            staffService.updateById(staff);

            // 变更会员服务门店
            if (!staff.getStoreId().equals(staffVO.getStoreId())) {
                log.info("导购门店变更触发会员服务门店变更 {} {} {}", staff.getId(), staff.getStoreId(), staffVO.getStoreId());
                ServerResponseEntity<List<UserApiVO>> userData = userFeignClient.getUserByStaffId(staff.getId());
                if (userData.isSuccess() && !CollectionUtils.isEmpty(userData.getData())) {
                    TzStore beforeStore = tzStoreService.getByStoreId(staffVO.getStoreId());
                    TzStore afterStore = tzStoreService.getByStoreId(staff.getStoreId());
                    tzStoreService.executeChange(beforeStore, userData.getData(),"同步任务", afterStore, 0, 1);
                }
            }
        }
    }
}
