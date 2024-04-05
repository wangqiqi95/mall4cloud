package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.auth.constant.SysTypeEnum;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventStatus;
import com.mall4j.cloud.api.platform.constant.OfflineHandleEventType;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.feign.OfflineHandleEventFeignClient;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.constant.AwardMode;
import com.mall4j.cloud.distribution.constant.DistributionSpuStatus;
import com.mall4j.cloud.distribution.constant.ParentAwardSet;
import com.mall4j.cloud.distribution.dto.DistributionSpuDTO;
import com.mall4j.cloud.distribution.model.DistributionSpu;
import com.mall4j.cloud.distribution.mapper.DistributionSpuMapper;
import com.mall4j.cloud.distribution.service.DistributionSpuService;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import io.seata.spring.annotation.GlobalTransactional;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 分销商品关联信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@Service
public class DistributionSpuServiceImpl implements DistributionSpuService {

    @Autowired
    private DistributionSpuMapper distributionSpuMapper;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OfflineHandleEventFeignClient offlineHandleEventFeignClient;

    /**
     * 比例最大存储值
     */
    private final static Long PROPORTION_MAX_STORAGE_VALUE = 10000L;

    @Override
    public DistributionSpuVO getByDistributionSpuId(Long distributionSpuId) {
        DistributionSpuVO distributionSpuVO = distributionSpuMapper.getByDistributionSpuId(distributionSpuId);
        ServerResponseEntity<SpuVO> spuRes = spuFeignClient.getDetailById(distributionSpuVO.getSpuId());
        if (spuRes.isFail()) {
            throw new LuckException(spuRes.getMsg());
        }
        distributionSpuVO.setSpuName(spuRes.getData().getName());
        distributionSpuVO.setMainImgUrl(spuRes.getData().getMainImgUrl());
        return distributionSpuVO;
    }

    @Override
    public void save(DistributionSpuDTO distributionSpuDTO) {
        this.validateDistributionSpu(distributionSpuDTO, true);
        DistributionSpu distributionSpu = mapperFacade.map(distributionSpuDTO, DistributionSpu.class);
        distributionSpuMapper.save(distributionSpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionSpuDTO distributionSpuDTO) {
        DistributionSpuVO byDistributionSpuId = distributionSpuMapper.getByDistributionSpuId(distributionSpuDTO.getDistributionSpuId());
        if (Objects.isNull(byDistributionSpuId)) {
            throw new LuckException("分销商品已不存在，请刷新页面");
        }
        this.validateDistributionSpu(distributionSpuDTO, false);
        DistributionSpu distributionSpu = mapperFacade.map(distributionSpuDTO, DistributionSpu.class);
        if (!Objects.equals(byDistributionSpuId.getSpuId(), distributionSpuDTO.getSpuId())) {
            distributionSpuMapper.deleteById(byDistributionSpuId.getDistributionSpuId());
            distributionSpu.setDistributionSpuId(byDistributionSpuId.getDistributionSpuId());
            distributionSpu.setState(byDistributionSpuId.getState());
            distributionSpuMapper.save(distributionSpu);
        } else {
            distributionSpuMapper.update(distributionSpu);
        }
    }

    @Override
    public void deleteById(Long distributionSpuId) {
        distributionSpuMapper.deleteById(distributionSpuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdAndShopId(Long distributionSpuId, Long shopId) {
        // 删除分销信息
        distributionSpuMapper.deleteByIdAndShopId(distributionSpuId, shopId);
        // 删除分销审核信息
        ServerResponseEntity<Void> offlineDeleteRes = offlineHandleEventFeignClient.deleteByHandleTypeAndHandleId(OfflineHandleEventType.DISTRIBUTION_PROD.getValue(), distributionSpuId);
        if (offlineDeleteRes.isFail()) {
            throw new LuckException(offlineDeleteRes.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void offline(OfflineHandleEventDTO offlineHandleEventDTO) {
        DistributionSpuVO distributionSpuVO = distributionSpuMapper.getByDistributionSpuId(offlineHandleEventDTO.getHandleId());
        if (Objects.isNull(distributionSpuVO)) {
            throw new LuckException("分销商品不存在，请刷新页面");
        }
        if (Objects.equals(distributionSpuVO.getState(), DistributionSpuStatus.PLATFORM_OFF_SHELF.value())) {
            throw new LuckException("该分销商品已下线，请刷新页面");
        }
        OfflineHandleEventDTO offlineHandleEvent = new OfflineHandleEventDTO();
        offlineHandleEvent.setHandleId(offlineHandleEventDTO.getHandleId());
        offlineHandleEvent.setHandleType(OfflineHandleEventType.DISTRIBUTION_PROD.getValue());
        offlineHandleEvent.setOfflineReason(offlineHandleEventDTO.getOfflineReason());
        ServerResponseEntity<Void> offlineRes = offlineHandleEventFeignClient.save(offlineHandleEvent);
        if (offlineRes.isFail()) {
            throw new LuckException(offlineRes.getMsg());
        }
        // 更新分销商品状态为下线状态
        int changeCount = distributionSpuMapper.changeStateByDistributionId(offlineHandleEventDTO.getHandleId(), distributionSpuVO.getState(), DistributionSpuStatus.PLATFORM_OFF_SHELF.value());
        if (changeCount != 1) {
            throw new LuckException("下线失败，请刷新页面后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void auditApply(OfflineHandleEventDTO offlineHandleEventDTO) {
        DistributionSpuVO distributionSpuVO = distributionSpuMapper.getByDistributionSpuId(offlineHandleEventDTO.getHandleId());
        if (!Objects.equals(AuthUserContext.get().getTenantId(), Constant.PLATFORM_SHOP_ID) && !Objects.equals(AuthUserContext.get().getTenantId(), distributionSpuVO.getShopId())) {
            throw new LuckException("该分销商品不属于当前店铺");
        }
        if (!Objects.equals(distributionSpuVO.getState(), DistributionSpuStatus.PLATFORM_OFF_SHELF.value())) {
            throw new LuckException("该分销商品状态已经发生变化，请刷新页面");
        }
        // 更新事件状态
        ServerResponseEntity<Void> offlineRes = offlineHandleEventFeignClient.updateToApply(offlineHandleEventDTO);
        if (offlineRes.isFail()) {
            throw new LuckException(offlineRes.getMsg());
        }
        // 更新分销商品状态
        int changeCount = distributionSpuMapper.changeStateByDistributionId(offlineHandleEventDTO.getHandleId(), distributionSpuVO.getState(), DistributionSpuStatus.WAITAUDIT.value());
        if (changeCount != 1) {
            throw new LuckException("申请失败，请刷新页面后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void audit(OfflineHandleEventDTO offlineHandleEventDTO) {
        DistributionSpuVO distributionSpuVO = distributionSpuMapper.getByDistributionSpuId(offlineHandleEventDTO.getHandleId());
        if (!Objects.equals(distributionSpuVO.getState(), DistributionSpuStatus.WAITAUDIT.value())) {
            throw new LuckException("该分销商品已经审核");
        }
        ServerResponseEntity<Void> offlineRes = offlineHandleEventFeignClient.auditOfflineEvent(offlineHandleEventDTO);
        if (offlineRes.isFail()) {
            throw new LuckException(offlineRes.getMsg());
        }
        Integer newState = 0;
        // 审核通过
        if (Objects.equals(offlineHandleEventDTO.getStatus(), OfflineHandleEventStatus.AGREE_BY_PLATFORM.getValue())) {
            newState = DistributionSpuStatus.PUT_SHELF.value();
        }
        // 审核不通过
        else if (Objects.equals(offlineHandleEventDTO.getStatus(), OfflineHandleEventStatus.DISAGREE_BY_PLATFORM.getValue())) {
            newState = DistributionSpuStatus.PLATFORM_OFF_SHELF.value();
        }
        int changeCount = distributionSpuMapper.changeStateByDistributionId(offlineHandleEventDTO.getHandleId(), distributionSpuVO.getState(), newState);
        if (changeCount != 1) {
            throw new LuckException("审核失败，请刷新页面后重试");
        }
    }

    @Override
    public DistributionSpuVO getBySpuId(Long spuId) {
        return distributionSpuMapper.getBySpuId(spuId);
    }

    @Override
    public void batchDeleteByIdsAndShopId(List<Long> distributionSpuIds, Long shopId) {

    }

    @Override
    public OfflineHandleEventVO getOfflineHandleEvent(Long distributionSpuId) {
        ServerResponseEntity<OfflineHandleEventVO> offlineRes = offlineHandleEventFeignClient.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.DISTRIBUTION_PROD.getValue(), distributionSpuId);
        if (offlineRes.isFail()) {
            throw new LuckException(offlineRes.getMsg());
        }
        return offlineRes.getData();
    }

    @Override
    public void updateState(Long distributionSpuId, Integer state) {
        DistributionSpuVO distributionSpuVO = distributionSpuMapper.getByDistributionSpuId(distributionSpuId);
        if (Objects.isNull(distributionSpuVO)) {
            throw new LuckException("分销商品不存在，请刷新页面");
        }
        if (Objects.equals(AuthUserContext.get().getSysType(), SysTypeEnum.MULTISHOP.value())) {
            // 商家端操作
            if (Objects.equals(distributionSpuVO.getState(), DistributionSpuStatus.PLATFORM_OFF_SHELF.value()) || Objects.equals(distributionSpuVO.getState(), DistributionSpuStatus.WAITAUDIT.value())) {
                throw new LuckException("当前分销商品状态无法更改");
            }
        }
        int changeCount = distributionSpuMapper.changeStateByDistributionId(distributionSpuId, distributionSpuVO.getState(), state);
        if (changeCount != 1) {
            throw new LuckException("状态变更失败，请刷新后重试");
        }
    }

    @Override
    public Boolean isStateBySpuIdAndState(Long spuId, Integer state) {
        int count = distributionSpuMapper.countBySpuIdAndState(spuId, state);
        return count > 0;
    }

    @Override
    public List<DistributionSpu> getByStateAndSpuIds(Integer state, List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return null;
        }
        return distributionSpuMapper.getByStateAndSpuIds(state,spuIds);
    }

    /**
     * 校验分销商品信息
     * @param distributionSpuDTO 分销商品信息
     * @param createOrUpdate true: 创建时校验    false: 更新时校验
     */
    private void validateDistributionSpu(DistributionSpuDTO distributionSpuDTO, boolean createOrUpdate) {
        // 根据商品id查询分销信息
        DistributionSpuVO distributionSpuVO = distributionSpuMapper.getBySpuId(distributionSpuDTO.getSpuId());
        // 判断分销信息是否已经存在
        if (createOrUpdate) {
            if (Objects.nonNull(distributionSpuVO)) {
                throw new LuckException("该商品已经添加到分销商品，不能重复添加");
            }
        }
        ServerResponseEntity<SpuVO> spuRes = spuFeignClient.getDetailById(distributionSpuDTO.getSpuId());
        if (spuRes.isFail()) {
            throw new LuckException(spuRes.getMsg());
        }
        SpuVO spuVO = spuRes.getData();
        if (Objects.isNull(spuVO)) {
            throw new LuckException("选择的商品不存在，请刷新后重试");
        }
        // 判断当前商品是否属于该店铺
        if (!Objects.equals(spuVO.getShopId(), distributionSpuDTO.getShopId())) {
            throw new LuckException("当前商品不属于你的店铺");
        }
        // 按比例
        if (Objects.equals(distributionSpuDTO.getAwardMode(), AwardMode.PROPORTION.value())) {
            // 校验直推奖励
            if (distributionSpuDTO.getAwardNumbers() > PROPORTION_MAX_STORAGE_VALUE) {
                throw new LuckException("直推奖励比例最大不能超过" + (PROPORTION_MAX_STORAGE_VALUE / 100) + "%");
            }
            if (Objects.equals(distributionSpuDTO.getParentAwardSet(), ParentAwardSet.OPEN.value())) {
                // 校验间推奖励
                if (distributionSpuDTO.getParentAwardNumbers() > PROPORTION_MAX_STORAGE_VALUE) {
                    throw new LuckException("间推奖励比例最大不能超过" + (PROPORTION_MAX_STORAGE_VALUE / 100) + "%");
                }
            }
        }else {
        // 按实际金额
            // 校验直推奖励
            if (distributionSpuDTO.getAwardNumbers() > spuVO.getPriceFee()) {
                throw new LuckException("直推奖励固定奖励不能超过商品原价格");
            }
            if (Objects.equals(distributionSpuDTO.getParentAwardSet(), ParentAwardSet.OPEN.value())) {
                // 校验间推奖励
                if (distributionSpuDTO.getParentAwardNumbers() > spuVO.getPriceFee()) {
                    throw new LuckException("间推奖励固定奖励不能超过商品原价格");
                }
            }
        }
    }
}
