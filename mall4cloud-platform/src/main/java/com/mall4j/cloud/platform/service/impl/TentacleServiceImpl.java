package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.auth.feign.AuthSocialFeignClient;
import com.mall4j.cloud.api.auth.vo.AuthSocialVO;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.model.Staff;
import com.mall4j.cloud.platform.model.Tentacle;
import com.mall4j.cloud.platform.mapper.TentacleMapper;
import com.mall4j.cloud.platform.service.StaffService;
import com.mall4j.cloud.platform.service.TentacleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

/**
 * 触点信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
@Slf4j
@Service
public class TentacleServiceImpl implements TentacleService {

    @Autowired
    private TentacleMapper tentacleMapper;

    @Autowired
    private AuthSocialFeignClient authSocialFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StaffService staffService;


    @Override
    public PageVO<Tentacle> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> tentacleMapper.list());
    }

    @Override
    public Tentacle getById(Long id) {
        return tentacleMapper.getById(id);
    }

    @Override
    public void save(Tentacle tentacle) {
        tentacleMapper.save(tentacle);
    }

    @Override
    public void update(Tentacle tentacle) {
        tentacleMapper.update(tentacle);
    }

    @Override
    public void deleteById(Long id) {
        tentacleMapper.deleteById(id);
    }

    /**
     * A 分享内容 给 B
     * B 点击访问小程序的时候  生成触点对象时 校验触点是否已经存在
     * 只有 C 端会员分发产生的触点 会调用
     */
    @Override
    public Tentacle findOrCreate(Long uid, Integer tentacleType) {
        log.info("生成触点开始 uid:{}, tentacleType:{}, startTime:{}", uid, tentacleType, System.currentTimeMillis());
        Tentacle tentacle = Optional.ofNullable(tentacleMapper.findByBusinessIdAndType(uid, tentacleType)).orElse(new Tentacle());
        //如果 触点已经存在  则返回
        if(tentacle.getId() != null) {
            return tentacle;
        }
        // 是员工触点 自己访问 则做更新操作
        tentacle.setBusinessId(uid);
        tentacle.setTentacleType(tentacleType);
        tentacle.setStatus(1);
        tentacle.setCreateTime(Optional.ofNullable(tentacle.getCreateTime()).orElse(new Date()));
        tentacle.setUpdateTime(new Date());
        tentacle.setOrgId(0L);
        splicingName(tentacle);
        tentacleMapper.save(tentacle);
        log.info("生成触点结束 uid:{}, tentacleType:{}, endTime:{}", uid, tentacleType, System.currentTimeMillis());
        return tentacle;
    }


    private void splicingName(Tentacle entity) {
        if (entity==null || entity.getTentacleType() == 1) {
            return;
        }
        if (entity.getTentacleType() == 2 || entity.getTentacleType() == 3){
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(entity.getBusinessId());
            if (!userData.isSuccess() || null == userData.getData()){
                return;
            }
            entity.setTentacleName(userData.getData().getNickName() + "_" + userData.getData().getUserMobile());
            return;
        }
        StaffVO staff = staffService.getById(entity.getBusinessId());
        if (staff != null || staff.getStatus() == 1){
            return;
        }
        entity.setOrgId(staff.getStoreId());
        entity.setGuideId(staff.getId());
        entity.setTentacleName(staff.getStaffName() + "_" + staff.getMobile());
    }

}
