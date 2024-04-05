package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoDto;
import com.mall4j.cloud.api.docking.jos.dto.MemberAndProtocolInfoResp;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoDto;
import com.mall4j.cloud.api.docking.jos.dto.QueryMemberAuditStatusByCertNoResp;
import com.mall4j.cloud.api.docking.jos.feign.MemberFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerDTO;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionCustomerSearchDTO;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionCustomerMapper;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionStoreMapper;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionStore;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionCustomerService;
import com.mall4j.cloud.distribution.vo.DistributionJointVentureCommissionCustomerStoreVO;
import com.mall4j.cloud.distribution.vo.DistributionJointVentureCommissionStoreCountVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 联营分佣客户service实现
 *
 * @author Zhang Fan
 * @date 2022/8/4 11:02
 */
@Service
public class DistributionJointVentureCommissionCustomerServiceImpl implements DistributionJointVentureCommissionCustomerService {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private MemberFeignClient memberFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private DistributionJointVentureCommissionCustomerMapper distributionJointVentureCommissionCustomerMapper;
    @Autowired
    private DistributionJointVentureCommissionStoreMapper distributionJointVentureCommissionStoreMapper;

    @Override
    public PageVO<DistributionJointVentureCommissionCustomer> page(PageDTO pageDTO, DistributionJointVentureCommissionCustomerSearchDTO distributionJointVentureCommissionCustomerSearchDTO) {
        PageVO<DistributionJointVentureCommissionCustomer> page = PageUtil.doPage(pageDTO, () -> distributionJointVentureCommissionCustomerMapper.list(distributionJointVentureCommissionCustomerSearchDTO));
        if (!CollectionUtils.isEmpty(page.getList())) {
            List<Long> limitStoreJointVentureIdList = page.getList().stream().filter(d -> d.getLimitStoreType() == 1)
                    .map(DistributionJointVentureCommissionCustomer::getId)
                    .collect(Collectors.toList());
            Map<Long, Integer> limitStoreCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(limitStoreJointVentureIdList)) {
                limitStoreCountMap = distributionJointVentureCommissionStoreMapper.countByJointVentureIdList(limitStoreJointVentureIdList)
                        .stream().collect(Collectors.toMap(DistributionJointVentureCommissionStoreCountVO::getJointVentureId,
                                DistributionJointVentureCommissionStoreCountVO::getNum));
            }
            Map<Long, Integer> finalLimitStoreCountMap = limitStoreCountMap;
            page.getList().stream().forEach(customer -> {
                Long activityId = customer.getId();
                if (finalLimitStoreCountMap.containsKey(activityId)) {
                    customer.setLimitStoreNum(finalLimitStoreCountMap.get(activityId));
                }
            });
        }
        return page;
    }

    @Override
    public DistributionJointVentureCommissionCustomer getById(Long id) {
        DistributionJointVentureCommissionCustomer byId = distributionJointVentureCommissionCustomerMapper.getById(id);
        if (byId != null && byId.getLimitStoreType() == 1) {
            byId.setLimitStoreNum(distributionJointVentureCommissionStoreMapper.countByJointVentureId(id));
        }
        // 第三方审核状态非通过，获取详情的时候再去调用第三方校验一次
        if (byId != null && byId.getThirdPartyStatus() != 1) {
            QueryMemberAuditStatusByCertNoDto queryMemberAuditStatusByCertNoDto = new QueryMemberAuditStatusByCertNoDto();
            queryMemberAuditStatusByCertNoDto.setCertType("201");
            queryMemberAuditStatusByCertNoDto.setCertNo(byId.getIdCard());
            QueryMemberAuditStatusByCertNoResp queryMemberAuditStatusByCertNoResp = memberFeignClient.queryMemberAuditStatusByCertNo(queryMemberAuditStatusByCertNoDto);
            if (queryMemberAuditStatusByCertNoResp.getCode() == 1) {
                Integer result = queryMemberAuditStatusByCertNoResp.getData().getResult();
                if (!byId.getThirdPartyStatus().equals(result)) {
                    byId.setThirdPartyStatus(result);
                    distributionJointVentureCommissionCustomerMapper.update(byId);
                }
            }
        }
        return byId;
    }

    @Override
    public DistributionJointVentureCommissionCustomer getByIdCard(String idCard) {
        return distributionJointVentureCommissionCustomerMapper.getByIdCard(idCard);
    }

    @Transactional
    @Override
    public void save(DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO) {
        DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer = mapperFacade.map(
                distributionJointVentureCommissionCustomerDTO, DistributionJointVentureCommissionCustomer.class);
        distributionJointVentureCommissionCustomer.setId(null);
        distributionJointVentureCommissionCustomer.setStatus(1);
        distributionJointVentureCommissionCustomer.setDeleted(0);
        distributionJointVentureCommissionCustomerMapper.save(distributionJointVentureCommissionCustomer);

        // 适用门店保存
        Long jointVentureId = distributionJointVentureCommissionCustomer.getId();
        Integer limitStoreType = distributionJointVentureCommissionCustomerDTO.getLimitStoreType();
        List<Long> limitStoreIdList = distributionJointVentureCommissionCustomerDTO.getLimitStoreIdList();
        if (limitStoreType == 1 && !CollectionUtils.isEmpty(limitStoreIdList)) {
            validateJointVentureStoreIsItCited(jointVentureId, limitStoreIdList);
            batchSaveDistributionJointVentureCommissionStore(jointVentureId, limitStoreIdList);
        }
        // 同步至京东益世
        MemberAndProtocolInfoResp memberAndProtocolInfoResp = syncToJDYS(distributionJointVentureCommissionCustomer);
        if (memberAndProtocolInfoResp.getCode() != 1) {
            throw new LuckException(memberAndProtocolInfoResp.getMsg());
        }
    }

    @Transactional
    @Override
    public void update(DistributionJointVentureCommissionCustomerDTO distributionJointVentureCommissionCustomerDTO) {
        DistributionJointVentureCommissionCustomer oldDistributionJointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper
                .getById(distributionJointVentureCommissionCustomerDTO.getId());
        if (Objects.isNull(oldDistributionJointVentureCommissionCustomer)) {
            throw new LuckException("联营分佣客户不存在");
        }
        DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer = mapperFacade.map(distributionJointVentureCommissionCustomerDTO,
                DistributionJointVentureCommissionCustomer.class);
        distributionJointVentureCommissionCustomerMapper.update(distributionJointVentureCommissionCustomer);
        Integer limitStoreType = distributionJointVentureCommissionCustomerDTO.getLimitStoreType();
        List<Long> limitStoreIdList = distributionJointVentureCommissionCustomerDTO.getLimitStoreIdList();
        List<Long> jointVentureStoreIdList = new ArrayList<>();
        if (limitStoreType == 0) {
            distributionJointVentureCommissionStoreMapper.deleteByJointVentureId(distributionJointVentureCommissionCustomer.getId());
        } else if (limitStoreType == 1 && !CollectionUtils.isEmpty(distributionJointVentureCommissionCustomerDTO.getLimitStoreIdList())) {
            // 适用门店保存
            List<DistributionJointVentureCommissionStore> distributionJointVentureCommissionStoreList = distributionJointVentureCommissionStoreMapper
                    .listByJointVentureId(distributionJointVentureCommissionCustomer.getId());
            if (!CollectionUtils.isEmpty(distributionJointVentureCommissionStoreList)) {
                jointVentureStoreIdList = distributionJointVentureCommissionStoreList.stream().map(DistributionJointVentureCommissionStore::getStoreId)
                        .collect(Collectors.toList());
            }
            List<Long> finalActivityStoreIdList = jointVentureStoreIdList;
            List<Long> diffStoreIdList = limitStoreIdList.stream().filter(t -> !finalActivityStoreIdList.contains(t)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(diffStoreIdList)) {
                validateJointVentureStoreIsItCited(distributionJointVentureCommissionCustomer.getId(), diffStoreIdList);
                batchSaveDistributionJointVentureCommissionStore(distributionJointVentureCommissionCustomer.getId(), diffStoreIdList);
            }
            List<Long> needDeleteStoreIdList = finalActivityStoreIdList.stream().filter(t -> !limitStoreIdList.contains(t)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(needDeleteStoreIdList)) {
                distributionJointVentureCommissionStoreMapper.batchDeleteByJointVentureIdAndStoreIds(
                        distributionJointVentureCommissionCustomer.getId(), needDeleteStoreIdList);
            }
        }
        // 京东益世审核未通过，编辑继续同步
        if (oldDistributionJointVentureCommissionCustomer.getThirdPartyStatus() != 1) {
            syncToJDYS(distributionJointVentureCommissionCustomer);
        }
    }

    private void batchSaveDistributionJointVentureCommissionStore(Long id, List<Long> limitStoreIdList) {
        List<DistributionJointVentureCommissionStore> jointVentureStoreList = limitStoreIdList.stream().map(storeId -> {
            DistributionJointVentureCommissionStore distributionJointVentureCommissionStore = new DistributionJointVentureCommissionStore();
            distributionJointVentureCommissionStore.setJointVentureId(id);
            distributionJointVentureCommissionStore.setStoreId(storeId);
            distributionJointVentureCommissionStore.setOrgId(0L);
            return distributionJointVentureCommissionStore;
        }).collect(Collectors.toList());
        distributionJointVentureCommissionStoreMapper.saveBatch(jointVentureStoreList);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        DistributionJointVentureCommissionCustomer byId = distributionJointVentureCommissionCustomerMapper.getById(id);
        if (byId == null) {
            throw new LuckException("联营分佣客户不存在");
        }
        byId.setStatus(status);
        distributionJointVentureCommissionCustomerMapper.update(byId);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        distributionJointVentureCommissionCustomerMapper.deleteById(id);
        distributionJointVentureCommissionStoreMapper.deleteByJointVentureId(id);
    }

    private void validateJointVentureStoreIsItCited(Long jointVentureId, List<Long> limitStoreIdList) {
        List<DistributionJointVentureCommissionCustomerStoreVO> citedInfo = distributionJointVentureCommissionStoreMapper.listJointVentureStoreIsItCited(jointVentureId, limitStoreIdList);
        if (citedInfo.size() > 0) {
            Map<String, List<DistributionJointVentureCommissionCustomerStoreVO>> citedInfoGroupByCustomerName = citedInfo.stream().collect(Collectors.groupingBy(DistributionJointVentureCommissionCustomerStoreVO::getCustomerName));
            List<Long> citedStoreIdList = citedInfo.stream().map(DistributionJointVentureCommissionCustomerStoreVO::getStoreId).collect(Collectors.toList());
            ServerResponseEntity<List<StoreVO>> storeVOList = storeFeignClient.listByStoreIdList(citedStoreIdList);
            if (storeVOList.isFail()) {
                throw new LuckException("部分门店已分配给其他联营客户");
            }
            Map<Long, List<StoreVO>> storeGroupByStoreId = storeVOList.getData().stream().collect(Collectors.groupingBy(StoreVO::getStoreId));
            StringBuilder exceptionTips = new StringBuilder("部分门店已分配给其他联营客户 ");
            for (Map.Entry<String, List<DistributionJointVentureCommissionCustomerStoreVO>> citedEntry : citedInfoGroupByCustomerName.entrySet()) {
                citedEntry.getValue().forEach(store ->
                        exceptionTips.append(
                                storeGroupByStoreId.get(store.getStoreId()).get(0).getName()
                        )
                                .append("(").append(citedEntry.getKey()).append(") "));
            }
            throw new LuckException(exceptionTips.toString());
        }
    }

    private MemberAndProtocolInfoResp syncToJDYS(DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MemberAndProtocolInfoDto memberAndProtocolInfoDto = new MemberAndProtocolInfoDto();
        memberAndProtocolInfoDto.setRequestId(UUID.randomUUID().toString().replace("-", ""));
        memberAndProtocolInfoDto.setName(distributionJointVentureCommissionCustomer.getCustomerName());
        memberAndProtocolInfoDto.setCertNo(distributionJointVentureCommissionCustomer.getIdCard());
        memberAndProtocolInfoDto.setRegisterTime(format.format(new Date()));
        memberAndProtocolInfoDto.setCertType("201");
        memberAndProtocolInfoDto.setNationality("156");
        memberAndProtocolInfoDto.setTelephone(distributionJointVentureCommissionCustomer.getPhone());
        memberAndProtocolInfoDto.setAddress(distributionJointVentureCommissionCustomer.getAddress());
        List<MemberAndProtocolInfoDto.MemberProtocal> memberProtocalList = new ArrayList<>();
        MemberAndProtocolInfoDto.MemberProtocal memberProtocal = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtocal.setFileType(0);
        memberProtocal.setFileFormat("0");
        memberProtocal.setFileInfo(distributionJointVentureCommissionCustomer.getIdCardPhotoFront());
        memberProtocal.setFileName(distributionJointVentureCommissionCustomer.getIdCardPhotoFront().substring(distributionJointVentureCommissionCustomer.getIdCardPhotoFront().lastIndexOf("/") + 1));
        MemberAndProtocolInfoDto.MemberProtocal memberProtoca2 = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtoca2.setFileType(1);
        memberProtoca2.setFileFormat("0");
        memberProtoca2.setFileInfo(distributionJointVentureCommissionCustomer.getIdCardPhotoBack());
        memberProtoca2.setFileName(distributionJointVentureCommissionCustomer.getIdCardPhotoBack().substring(distributionJointVentureCommissionCustomer.getIdCardPhotoBack().lastIndexOf("/") + 1));
        MemberAndProtocolInfoDto.MemberProtocal memberProtoca3 = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtoca3.setFileType(2);
        memberProtoca3.setFileFormat("0");
        memberProtoca3.setFileInfo(distributionJointVentureCommissionCustomer.getIdCardPhotoBack());
        memberProtoca3.setFileName(distributionJointVentureCommissionCustomer.getIdCardPhotoBack().substring(distributionJointVentureCommissionCustomer.getIdCardPhotoBack().lastIndexOf("/") + 1));
        MemberAndProtocolInfoDto.MemberProtocal memberProtoca4 = new MemberAndProtocolInfoDto.MemberProtocal();
        memberProtoca4.setFileType(3);
        memberProtoca4.setFileFormat("0");
        memberProtoca4.setFileInfo(distributionJointVentureCommissionCustomer.getIdCardPhotoBack());
        memberProtoca4.setFileName(distributionJointVentureCommissionCustomer.getIdCardPhotoBack().substring(distributionJointVentureCommissionCustomer.getIdCardPhotoBack().lastIndexOf("/") + 1));
        memberProtocalList.add(memberProtocal);
        memberProtocalList.add(memberProtoca2);
        memberProtocalList.add(memberProtoca3);
        memberProtocalList.add(memberProtoca4);
        memberAndProtocolInfoDto.setMemberProtocalList(memberProtocalList);
        return memberFeignClient.memberAndProtocoInfo(memberAndProtocolInfoDto);
    }
}
