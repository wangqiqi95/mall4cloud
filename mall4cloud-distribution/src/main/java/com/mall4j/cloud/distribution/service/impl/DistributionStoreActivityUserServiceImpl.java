package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.feign.WxMaApiFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.common.constant.MaSendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserDTO;
import com.mall4j.cloud.distribution.dto.DistributionStoreActivityUserQueryDTO;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivityMapper;
import com.mall4j.cloud.distribution.mapper.DistributionStoreActivityUserMapper;
import com.mall4j.cloud.distribution.model.DistributionStoreActivity;
import com.mall4j.cloud.distribution.model.DistributionStoreActivityUser;
import com.mall4j.cloud.distribution.service.DistributionStoreActivityUserService;
import com.mall4j.cloud.distribution.vo.DistributionStoreActivityUserExcelVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 门店活动-报名用户
 *
 * @author gww
 * @date 2021-12-27 13:42:16
 */
@Service
public class DistributionStoreActivityUserServiceImpl implements DistributionStoreActivityUserService {

    @Resource
    private DistributionStoreActivityUserMapper distributionStoreActivityUserMapper;
    @Resource
    private DistributionStoreActivityMapper distributionStoreActivityMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;
    @Autowired
    WxMaApiFeignClient wxMaApiFeignClient;

    @Override
    public PageVO<DistributionStoreActivityUser> page(PageDTO pageDTO, DistributionStoreActivityUserQueryDTO distributionStoreActivityUserQueryDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionStoreActivityUserMapper.list(distributionStoreActivityUserQueryDTO));
    }

    @Override
    public void export(HttpServletResponse response, DistributionStoreActivityUserQueryDTO distributionStoreActivityUserQueryDTO) {
        List<DistributionStoreActivityUser> distributionStoreActivityUserList = distributionStoreActivityUserMapper.list(distributionStoreActivityUserQueryDTO);
        if (!CollectionUtils.isEmpty(distributionStoreActivityUserList)) {
            ExcelUtil.soleExcel(response, distributionStoreActivityUserList.stream().map(this :: buildDistributionStoreActivityUserExcelVO).collect(Collectors.toList()),
                    DistributionStoreActivityUserExcelVO.EXCEL_NAME, DistributionStoreActivityUserExcelVO.MERGE_ROW_INDEX,
                    DistributionStoreActivityUserExcelVO.MERGE_COLUMN_INDEX, DistributionStoreActivityUserExcelVO.class);
        } else {
            ExcelUtil.soleExcel(response, new ArrayList(),
                    DistributionStoreActivityUserExcelVO.EXCEL_NAME, DistributionStoreActivityUserExcelVO.MERGE_ROW_INDEX,
                    DistributionStoreActivityUserExcelVO.MERGE_COLUMN_INDEX, DistributionStoreActivityUserExcelVO.class);
        }
    }

    private DistributionStoreActivityUserExcelVO buildDistributionStoreActivityUserExcelVO(DistributionStoreActivityUser distributionStoreActivityUser) {
        DistributionStoreActivityUserExcelVO distributionStoreActivityUserExcelVO = mapperFacade.map(distributionStoreActivityUser,
                DistributionStoreActivityUserExcelVO.class);
        if (distributionStoreActivityUser.getUserGender() != null) {
            if (distributionStoreActivityUser.getUserGender() == 1) {
                distributionStoreActivityUserExcelVO.setGender("男");
            }
            if (distributionStoreActivityUser.getUserGender() == 2) {
                distributionStoreActivityUserExcelVO.setGender("女");
            }
        }
        if (distributionStoreActivityUser.getStatus() == 1) {
            distributionStoreActivityUserExcelVO.setCancelTime(distributionStoreActivityUser.getUpdateTime());
            distributionStoreActivityUserExcelVO.setActivityStatus("已取消");
        } else {
            distributionStoreActivityUserExcelVO.setActivityStatus("已报名");
        }

        return distributionStoreActivityUserExcelVO;
    }

    @Override
    public void save(DistributionStoreActivityUserDTO distributionStoreActivityUserDTO) {
        Long userId = AuthUserContext.get().getUserId();
        Long activityId = distributionStoreActivityUserDTO.getActivityId();
        DistributionStoreActivity distributionStoreActivity = distributionStoreActivityMapper.getById(activityId);
        if (Objects.isNull(distributionStoreActivity)) {
            throw new LuckException("门店活动不存在");
        }
        Date now = new Date();
        if (now.before(distributionStoreActivity.getApplyStartTime())) {
            throw new LuckException("报名还未开始");
        }
        if (now.after(distributionStoreActivity.getApplyEndTime())) {
            throw new LuckException("报名时间已过，下次来早点哦~");
        }
        int count = distributionStoreActivityUserMapper.countByActivityIdAndStatus(activityId, 0) + 1;
        if (count > distributionStoreActivity.getLimitApplyNum()) {
            throw new LuckException("门店活动人数已满");
        }
        if (distributionStoreActivity.getNeedAge() == 1 && distributionStoreActivityUserDTO.getUserAge() == null) {
            throw new LuckException("年龄必须填写");
        }
        if (distributionStoreActivity.getNeedIdCard() == 1 && StrUtil.isEmpty(distributionStoreActivityUserDTO.getUserIdCard())) {
            throw new LuckException("证件号必须填写");
        }
        DistributionStoreActivityUser mobileCheck = distributionStoreActivityUserMapper.findByActivityIdAndUserMobile(activityId,
                distributionStoreActivityUserDTO.getUserMobile());
        if (Objects.nonNull(mobileCheck) && mobileCheck.getStatus() == 0) {
            throw new LuckException("不能重复申请，该手机号已经报过名");
        }
        DistributionStoreActivityUser old = distributionStoreActivityUserMapper.findByUserIdAndActivityId(userId, activityId);
        if (Objects.nonNull(old) && old.getStatus() == 0) {
            throw new LuckException("不能重复申请");
        }
        DistributionStoreActivityUser distributionStoreActivityUser = mapperFacade.map(distributionStoreActivityUserDTO,
                DistributionStoreActivityUser.class);
        distributionStoreActivityUser.setId(null);
        if (Objects.nonNull(old)) {
            distributionStoreActivityUser.setId(old.getId());
        }
        distributionStoreActivityUser.setUserId(userId);
        distributionStoreActivityUser.setStatus(0);
        distributionStoreActivityUser.setSignStatus(0);
        distributionStoreActivityUser.setOrgId(0l);
        if (StrUtil.isNotBlank(distributionStoreActivityUserDTO.getTentacleNo())) {
            ServerResponseEntity<TentacleContentVO> tentacleResp = tentacleContentFeignClient.findByTentacleNo(distributionStoreActivityUserDTO.getTentacleNo());
            if (tentacleResp.isSuccess() && tentacleResp.getData().getTentacle().getTentacleType() == 4) {
                Long staffId = tentacleResp.getData().getTentacle().getBusinessId();
                ServerResponseEntity<StaffVO> staffRep = staffFeignClient.getStaffById(staffId);
                if (staffRep.isSuccess() && Objects.nonNull(staffRep.getData())) {
                    distributionStoreActivityUser.setUserStaffId(staffId);
                    distributionStoreActivityUser.setUserStaffName(staffRep.getData().getStaffName());
                }
            }
        }
        if (distributionStoreActivityUser.getId() != null) {
            distributionStoreActivityUserMapper.update(distributionStoreActivityUser);
        } else {
            distributionStoreActivityUserMapper.save(distributionStoreActivityUser);
        }
    }

    @Override
    public void applyCheck(Long activityId) {
        Long userId = AuthUserContext.get().getUserId();
        DistributionStoreActivity distributionStoreActivity = distributionStoreActivityMapper.getById(activityId);
        if (Objects.isNull(distributionStoreActivity)) {
            throw new LuckException("门店活动不存在");
        }
        Date now = new Date();
        if (now.before(distributionStoreActivity.getApplyStartTime())) {
            throw new LuckException("报名还未开始");
        }
        if (now.after(distributionStoreActivity.getApplyEndTime())) {
            throw new LuckException("报名时间已过，下次来早点哦~");
        }
        int count = distributionStoreActivityUserMapper.countByActivityIdAndStatus(activityId, 0) + 1;
        if (count > distributionStoreActivity.getLimitApplyNum()) {
            throw new LuckException("门店活动人数已满");
        }
        DistributionStoreActivityUser old = distributionStoreActivityUserMapper.findByUserIdAndActivityId(userId, activityId);
        if (Objects.nonNull(old) && old.getStatus() == 0) {
            throw new LuckException("不能重复申请");
        }
    }

    @Override
    public void cancel(Long userId, Long activityId) {
        DistributionStoreActivityUser distributionStoreActivityUser = distributionStoreActivityUserMapper.findByUserIdAndActivityId(userId,
                activityId);
        if (Objects.isNull(distributionStoreActivityUser)) {
            throw new LuckException("您并未报名该活动，不能取消");
        }
        if (distributionStoreActivityUser.getStatus() != null && distributionStoreActivityUser.getStatus() == 1) {
            throw new LuckException("不能重复取消");
        }
        distributionStoreActivityUserMapper.cancel(userId, activityId);
        //会员取消报名的话，同时取消已经订阅的纪录，不发订阅消息。
        wxMaApiFeignClient.cancelUserSubscriptRecord(userId, MaSendTypeEnum.ACTIVITY_BEGIN.getValue(),activityId);
        wxMaApiFeignClient.cancelUserSubscriptRecord(userId,MaSendTypeEnum.ACTIVITY_END.getValue(),activityId);
    }

    @Override
    public void sign(Long userId, Long activityId) {
        DistributionStoreActivityUser distributionStoreActivityUser = distributionStoreActivityUserMapper.findByUserIdAndActivityId(userId,
                activityId);
        if (Objects.isNull(distributionStoreActivityUser)) {
            throw new LuckException("您并未报名该活动，不能签到");
        }
        if (distributionStoreActivityUser.getSignStatus() != null && distributionStoreActivityUser.getSignStatus() == 1) {
            throw new LuckException("不能重复签到");
        }
        distributionStoreActivityUserMapper.sign(userId, activityId);
    }

    @Override
    public DistributionStoreActivityUser findByUserIdAndActivityId(Long userId, Long activityId) {
        return distributionStoreActivityUserMapper.findByUserIdAndActivityId(userId, activityId);
    }

    @Override
    public Integer countByActivityIdAndStatus(Long activityId, Integer status) {
        return distributionStoreActivityUserMapper.countByActivityIdAndStatus(activityId, status);
    }
}
