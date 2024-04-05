package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.util.IdUtil;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoDto;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberStatusResp;
import com.mall4j.cloud.api.docking.jos.feign.MemberFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountAuthDTO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionAccountAuthMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccountAuth;
import com.mall4j.cloud.distribution.service.DistributionCommissionAccountAuthService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 佣金管理-佣金账户-认证
 *
 * @author gww
 * @date 2022-01-31 12:15:41
 */
@Service
public class DistributionCommissionAccountAuthServiceImpl implements DistributionCommissionAccountAuthService {

    @Autowired
    private DistributionCommissionAccountAuthMapper distributionCommissionAccountAuthMapper;
    @Autowired
    private MemberFeignClient memberFeignClient;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public DistributionCommissionAccountAuth getByIdentityTypeAndUserId(Integer identityType, Long userId) {
        DistributionCommissionAccountAuth distributionCommissionAccountAuth = distributionCommissionAccountAuthMapper.getByIdentityTypeAndUserId(identityType,
                userId);
        if (Objects.nonNull(distributionCommissionAccountAuth) && distributionCommissionAccountAuth.getAuthStatus() != null &&
                distributionCommissionAccountAuth.getAuthStatus() == 3) {
            QueryMemberStatusDto queryMemberStatusDto = new QueryMemberStatusDto();
            queryMemberStatusDto.setRequestId(distributionCommissionAccountAuth.getRequestId());
            QueryMemberStatusResp queryMemberStatusResp = memberFeignClient.queryMemberStatus(queryMemberStatusDto);
            if (queryMemberStatusResp.getCode() == 1) {
                Integer status = queryMemberStatusResp.getData().getResult();
                String  remark = queryMemberStatusResp.getData().getRemark();
                if (status != distributionCommissionAccountAuth.getAuthStatus()) {
                    distributionCommissionAccountAuth.setAuthStatus(status);
                    distributionCommissionAccountAuth.setAuthFailMsg(remark);
                    distributionCommissionAccountAuthMapper.update(distributionCommissionAccountAuth);
                }
            }
        }
        return distributionCommissionAccountAuth;
    }

    @Override
    public void auth(DistributionCommissionAccountAuthDTO commissionAccountAuthDTO) {
        Long userId = commissionAccountAuthDTO.getUserId();
        Integer identityType = commissionAccountAuthDTO.getIdentityType();
        String requestId = String.valueOf(IdUtil.getSnowflake().nextId());
        DistributionCommissionAccountAuth distributionCommissionAccountAuth = distributionCommissionAccountAuthMapper.getByIdentityTypeAndUserId(identityType,
                userId);
        DistributionCommissionAccountAuth saveOrUpdate = mapperFacade.map(commissionAccountAuthDTO, DistributionCommissionAccountAuth.class);
        saveOrUpdate.setRequestId(requestId);
        if (Objects.nonNull(distributionCommissionAccountAuth)) {
            saveOrUpdate.setCreateTime(distributionCommissionAccountAuth.getCreateTime());
            saveOrUpdate.setId(distributionCommissionAccountAuth.getId());
        } else {
            saveOrUpdate.setCreateTime(new Date());
            saveOrUpdate.setAuthStatus(3);
        }
        MemberAndProtocolInfoResp memberAndProtocolInfoResp = syncToJDYS(saveOrUpdate);
        if (memberAndProtocolInfoResp.getCode() != 1) {
            throw new LuckException(memberAndProtocolInfoResp.getMsg());
        }
        if (Objects.nonNull(distributionCommissionAccountAuth)) {
            distributionCommissionAccountAuthMapper.update(saveOrUpdate);
        } else {
            distributionCommissionAccountAuthMapper.save(saveOrUpdate);
        }
    }

    private MemberAndProtocolInfoResp syncToJDYS(DistributionCommissionAccountAuth distributionCommissionAccountAuth) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MemberAndProtocolInfoDto memberAndProtocolInfoDto = new MemberAndProtocolInfoDto();
        memberAndProtocolInfoDto.setRequestId(distributionCommissionAccountAuth.getRequestId());
        memberAndProtocolInfoDto.setName(distributionCommissionAccountAuth.getName());
        memberAndProtocolInfoDto.setCertNo(distributionCommissionAccountAuth.getCertNo());
        memberAndProtocolInfoDto.setRegisterTime(format.format(distributionCommissionAccountAuth.getCreateTime()));
        memberAndProtocolInfoDto.setCertType("201");
        memberAndProtocolInfoDto.setNationality("156");
        memberAndProtocolInfoDto.setTelephone(distributionCommissionAccountAuth.getTelephone());
        memberAndProtocolInfoDto.setAddress(distributionCommissionAccountAuth.getAddress());
        List<MemberAndProtocolInfoDto.MemberProtocal> memberProtocalList = new ArrayList<>();
        MemberAndProtocolInfoDto.MemberProtocal memberProtocal = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtocal.setFileType(0);
        memberProtocal.setFileFormat("0");
        memberProtocal.setFileInfo(distributionCommissionAccountAuth.getIdPhotoFront());
        memberProtocal.setFileName(distributionCommissionAccountAuth.getIdPhotoFront().substring(distributionCommissionAccountAuth.getIdPhotoFront().lastIndexOf("/") + 1));
        MemberAndProtocolInfoDto.MemberProtocal memberProtoca2 = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtoca2.setFileType(1);
        memberProtoca2.setFileFormat("0");
        memberProtoca2.setFileInfo(distributionCommissionAccountAuth.getIdPhotoBack());
        memberProtoca2.setFileName(distributionCommissionAccountAuth.getIdPhotoBack().substring(distributionCommissionAccountAuth.getIdPhotoBack().lastIndexOf("/") + 1));
        MemberAndProtocolInfoDto.MemberProtocal memberProtoca3 = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtoca3.setFileType(2);
        memberProtoca3.setFileFormat("0");
        memberProtoca3.setFileInfo(distributionCommissionAccountAuth.getIdPhotoBack());
        memberProtoca3.setFileName(distributionCommissionAccountAuth.getIdPhotoBack().substring(distributionCommissionAccountAuth.getIdPhotoBack().lastIndexOf("/") + 1));
        MemberAndProtocolInfoDto.MemberProtocal memberProtoca4 = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtoca4.setFileType(3);
        memberProtoca4.setFileFormat("0");
        memberProtoca4.setFileInfo(distributionCommissionAccountAuth.getIdPhotoBack());
        memberProtoca4.setFileName(distributionCommissionAccountAuth.getIdPhotoBack().substring(distributionCommissionAccountAuth.getIdPhotoBack().lastIndexOf("/") + 1));
        memberProtocalList.add(memberProtocal);
        memberProtocalList.add(memberProtoca2);
        memberProtocalList.add(memberProtoca3);
        memberProtocalList.add(memberProtoca4);
        memberAndProtocolInfoDto.setMemberProtocalList(memberProtocalList);
        return memberFeignClient.memberAndProtocoInfo(memberAndProtocolInfoDto);
    }

    @Override
    public void save(DistributionCommissionAccountAuth distributionCommissionAccountAuth) {
        distributionCommissionAccountAuthMapper.save(distributionCommissionAccountAuth);
    }

    @Override
    public void update(DistributionCommissionAccountAuth distributionCommissionAccountAuth) {
        distributionCommissionAccountAuthMapper.update(distributionCommissionAccountAuth);
    }

}
