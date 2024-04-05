package com.mall4j.cloud.platform.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.auth.feign.AccountFeignClient;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.platform.dto.StaffSyncDTO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.rbac.feign.UserRoleFeignClient;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.DateUtil;
import com.mall4j.cloud.platform.model.*;
import com.mall4j.cloud.platform.service.StaffOrgRelService;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StaffSyncManager {

    @Autowired
    private StaffService staffService;
    @Autowired
    private SegmentFeignClient segmentFeignClient;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StaffOrgRelService staffOrgRelService;
    @Autowired
    private AccountFeignClient accountFeignClient;
    @Autowired
    private UserRoleFeignClient userRoleFeignClient;
    @Autowired
    private UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;

    @Async
    public void staffSyncMessage(StaffSyncDTO staffSyncDTO) {
        log.info("staff sync param : {} ", JSONObject.toJSONString(staffSyncDTO));

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
            staff.setAvatar(staffSyncDTO.getAvater());
            staff.setStatus(staffSyncDTO.getIsLeave().equalsIgnoreCase("Y") ? 1 : 0);
            staffService.add(staff);
        } else {
            Staff staff = new Staff();
            staff.setId(staffVO.getId());
            staff.setMobile(staffSyncDTO.getMobile());
            staff.setQiWeiUserStatus(staffSyncDTO.getQiweiUserStatus());
            staff.setSysUserId(staffVO.getSysUserId());
            if(staff.getQiWeiUserStatus()==5&& CharSequenceUtil.isNotEmpty(staffSyncDTO.getCreateTime())){//离职
                staff.setDimissionTime(DateUtil.formatDateWX(staffSyncDTO.getCreateTime()));
                staff.setIsDelete(1);
                staff.setUpdateTime(new Date());
                staff.setUpdateBy("成员退出企业");
                staff.setUpdateTime(new Date());
                staff.setStatus(1);
            }
            if (StringUtils.isNotBlank(staffSyncDTO.getIsLeave())) {
                staff.setStatus(staffSyncDTO.getIsLeave().equalsIgnoreCase("Y") ? 1 : 0);
            }
            staff.setUpdateTime(new Date());
            staff.setAvatar(staffSyncDTO.getAvater());
            if(CollUtil.isNotEmpty(staffSyncDTO.getOrgIds())){
                staff.setOrgId(StrUtil.join(",",staffSyncDTO.getOrgIds()));
            }
            staffService.updateById(staff);

            //更新sys_user表部门信息
//            if(StrUtil.isNotBlank(staff.getSysUserId())){
//                SysUser sysUser=new SysUser();
//                sysUser.setSysUserId(Long.parseLong(staff.getSysUserId()));
//                if(CollUtil.isNotEmpty(staffSyncDTO.getOrgIds())){
//                    sysUser.setOrgId(StrUtil.join(",",staffSyncDTO.getOrgIds()));
//                }else{
//                    sysUser.setOrgId(null);
//                }
//                sysUser.setStatus(staffSyncDTO.getQiweiUserStatus());
//                sysUser.setUpdateBy("企微推送更新部门");
//                sysUserService.updateById(sysUser);
//
//                if(staff.getQiWeiUserStatus()==5&& CharSequenceUtil.isNotEmpty(staffSyncDTO.getCreateTime())){//离职
//                    deleteUser(staff);
//                }
//            }


            //更新staff_org_rel信息
//            if(CollUtil.isNotEmpty(staffSyncDTO.getOrgIds())){
//                //员工关联部门
//                List<Long> orgIds= Arrays.stream(staff.getOrgId().split(",")).map(s->Long.parseLong(s)).collect(Collectors.toList());
//                List<StaffOrgRel> orgRels=new ArrayList<>();
//                for (Long orgId : orgIds) {
//                    StaffOrgRel orgRel=new StaffOrgRel();
//                    orgRel.setOrgId(orgId);
//                    orgRel.setStaffId(staff.getId());
//                    orgRel.setCreateBy(AuthUserContext.get().getUsername());
//                    orgRel.setCreateDate(new Date());
//                    orgRel.setIsDelete(0);
//                    orgRels.add(orgRel);
//                }
//                staffOrgRelService.deleteByStaffId(staff.getId());
//                staffOrgRelService.saveBatch(orgRels);
//            }

            //员工离职
            if(Objects.nonNull(staff.getStatus()) && staff.getStatus()==1){
                //更新好友关系表数据
                userStaffCpRelationFeignClient.staffDimission(Arrays.asList(staff.getId()));
            }
        }
    }

    /**
     * 删除账号信息
     * @param staff
     */
//    private void deleteUser(Staff staff){
//        Long sysUserId=Long.parseLong(staff.getSysUserId());
//        ServerResponseEntity<Void> responseEntity = accountFeignClient.deleteByUserId(sysUserId);
//        log.info("StaffSyncManager deleteUser response:{}",responseEntity.toString());
//        if (!responseEntity.isSuccess()) {
//            throw new LuckException(responseEntity.getMsg());
//        }
//        userRoleFeignClient.deleteByUserIdAndSysType(sysUserId);
//        SysUser sysUser=new SysUser();
//        sysUser.setSysUserId(sysUserId);
//        sysUser.setStatus(1);
//        sysUser.setIsDelete(1);
//        sysUser.setUpdateTime(new Date());
//        sysUser.setUpdateBy("成员退出企业");
//        sysUserService.updateById(sysUser);
//    }
}
