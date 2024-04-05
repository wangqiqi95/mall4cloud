package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionPurchaseRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionShareRecordDTO;
import com.mall4j.cloud.distribution.dto.PurchaseRankingDTO;
import com.mall4j.cloud.distribution.mapper.DistributionPurchaseRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionPurchaseRecord;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import com.mall4j.cloud.distribution.service.DistributionPurchaseRecordService;
import com.mall4j.cloud.distribution.service.DistributionUnreadRecordService;
import com.mall4j.cloud.distribution.vo.DistributionPromotionGroupVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import com.mall4j.cloud.distribution.vo.DistributionPurchaseRecordVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-加购记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Service
public class DistributionPurchaseRecordServiceImpl implements DistributionPurchaseRecordService {

    @Autowired
    private DistributionPurchaseRecordMapper distributionPurchaseRecordMapper;

    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;

    @Autowired
    private OnsMQTemplate sendDistributionActivityShareTemplate;

    @Autowired
    private OnsMQTemplate sendDistributionGuideShareTemplate;

    @Autowired
    private DistributionUnreadRecordService distributionUnreadRecordService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;


    @Override
    public PageVO<DistributionPurchaseRecordVO> page(PageDTO pageDTO, DistributionPurchaseRecordDTO dto) {
        DistributionUnreadRecord unreadRecord = distributionUnreadRecordService.getByUser(dto.getShareType(), dto.getShareId());
        if (null != unreadRecord){
            unreadRecord.setUnreadPurchaseNum(0);
            distributionUnreadRecordService.update(unreadRecord);
        }
        if (StringUtils.isNotEmpty(dto.getKeywords())){
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(dto.getKeywords());
            if (userData.isSuccess() && null != userData.getData()){
                dto.setPurchaseId(userData.getData().getUserId());
            } else {
                dto.setPurchaseId(-1L);
            }
        }
        PageVO<DistributionPurchaseRecordVO> page = PageUtil.doPage(pageDTO, () -> distributionPurchaseRecordMapper.list(dto));
        if (CollectionUtils.isEmpty(page.getList())){
            return page;
        }
        page.getList().forEach(distributionPurchaseRecordVO -> {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(distributionPurchaseRecordVO.getPurchaseId());
            if (userData.isSuccess() && null != userData.getData()){
                distributionPurchaseRecordVO.setPurchaseMobile(userData.getData().getUserMobile());
                distributionPurchaseRecordVO.setPurchaseName(userData.getData().getNickName());
            }
        });
        return page;
    }

    @Override
    public DistributionPurchaseRecord getById(Long id) {
        return distributionPurchaseRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionPurchaseRecord distributionPurchaseRecord) {

        if (StringUtils.isNotEmpty(distributionPurchaseRecord.getTentacleNo())) {
            ServerResponseEntity<TentacleContentVO> responseEntity = tentacleContentFeignClient.findByTentacleNo(distributionPurchaseRecord.getTentacleNo());
            if (responseEntity.isSuccess() && null != responseEntity.getData() && null != responseEntity.getData().getTentacle()) {
                if (responseEntity.getData().getTentacle().getTentacleType() == 4){
                    distributionPurchaseRecord.setShareType(1);
                    distributionPurchaseRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                } else if (responseEntity.getData().getTentacle().getTentacleType() == 3){
                    distributionPurchaseRecord.setShareType(2);
                    distributionPurchaseRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                } else if (responseEntity.getData().getTentacle().getTentacleType() == 2){
                    distributionPurchaseRecord.setShareType(3);
                    distributionPurchaseRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                }
            }
        }
        distributionPurchaseRecordMapper.save(distributionPurchaseRecord);

        saveActivityShareRecord(distributionPurchaseRecord);

        saveGuideShareRecord(distributionPurchaseRecord);

        if (null != distributionPurchaseRecord.getShareId()){
            DistributionUnreadRecord record = new DistributionUnreadRecord();
            record.setUserId(distributionPurchaseRecord.getShareId());
            record.setIdentityType(distributionPurchaseRecord.getShareType());
            record.setUnreadPurchaseNum(1);
            sendDistributionGuideShareTemplate.syncSend(record);
        }

    }

    private void saveGuideShareRecord(DistributionPurchaseRecord distributionPurchaseRecord) {
        if (null == distributionPurchaseRecord.getShareId()){
            return;
        }
        if (null == distributionPurchaseRecord.getShareType()){
            return;
        }
        if (1 != distributionPurchaseRecord.getShareType()){
            return;
        }
        DistributionShareRecordDTO dto = new DistributionShareRecordDTO();
        dto.setActivityId(distributionPurchaseRecord.getActivityId());
        dto.setActivityType(distributionPurchaseRecord.getActivityType());
        dto.setRecordType(3);
        dto.setExist(distributionPurchaseRecordMapper.getByPurchaseAndActivity(distributionPurchaseRecord.getPurchaseId(), distributionPurchaseRecord.getPurchaseType(), distributionPurchaseRecord.getActivityId(), distributionPurchaseRecord.getActivityType()) != null);
        dto.setShareId(distributionPurchaseRecord.getShareId());
        sendDistributionGuideShareTemplate.syncSend(dto);
    }

    private void saveActivityShareRecord(DistributionPurchaseRecord distributionPurchaseRecord) {
        DistributionActivityShareRecordDTO dto = new DistributionActivityShareRecordDTO();
        dto.setActivityType(distributionPurchaseRecord.getActivityType());

        if (distributionPurchaseRecord.getActivityType() == 4){
            dto.setActivityId(distributionPurchaseRecord.getProductId());
            dto.setActivityName(distributionPurchaseRecord.getProductName());
        } else {
            dto.setActivityId(distributionPurchaseRecord.getActivityId());
            dto.setActivityName(distributionPurchaseRecord.getActivityName());
        }
        dto.setRecordType(3);
        dto.setExist(distributionPurchaseRecordMapper.getByPurchaseAndActivity(distributionPurchaseRecord.getPurchaseId(), distributionPurchaseRecord.getPurchaseType(), distributionPurchaseRecord.getActivityId(), distributionPurchaseRecord.getActivityType()) != null);
        sendDistributionActivityShareTemplate.syncSend(dto);
    }

    @Override
    public void update(DistributionPurchaseRecord distributionPurchaseRecord) {
        distributionPurchaseRecordMapper.update(distributionPurchaseRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionPurchaseRecordMapper.deleteById(id);
    }

    @Override
    public Integer countNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionPurchaseRecordMapper.countNumByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public Integer countNumByShareActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionPurchaseRecordMapper.countNumByShareActivityAndDate(shareId, activityId, activityType, startDate, endDate);
    }

    @Override
    public Integer countUserNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionPurchaseRecordMapper.countUserNumByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public Integer countUserNumByShareActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionPurchaseRecordMapper.countUserNumByShareActivityAndDate(shareId, activityId, activityType, startDate, endDate);
    }

    @Override
    public DistributionPromotionStatVO stat(Integer shareType, Long shareId) {
        DistributionPromotionStatVO distributionPromotionStatVO = new DistributionPromotionStatVO();
        List<DistributionPromotionGroupVO> distributionPromotionGroupVOList =  distributionPurchaseRecordMapper.groupByActivityType(shareType,
                shareId);
        if (CollectionUtils.isNotEmpty(distributionPromotionGroupVOList)) {
            Integer total = distributionPromotionGroupVOList.stream().mapToInt(DistributionPromotionGroupVO :: getCount).sum();
            Integer poster = distributionPromotionGroupVOList.stream().filter(dp -> dp.getActivityType() != null
                    && dp.getActivityType() == 1).mapToInt(DistributionPromotionGroupVO :: getCount).sum();
            Integer specialSubject = distributionPromotionGroupVOList.stream().filter(dp -> dp.getActivityType() != null &&
                    dp.getActivityType() == 2).mapToInt(DistributionPromotionGroupVO :: getCount).sum();
            Integer moments = distributionPromotionGroupVOList.stream().filter(dp -> dp.getActivityType() != null &&
                    dp.getActivityType() == 3).mapToInt(DistributionPromotionGroupVO :: getCount).sum();
            Integer product = distributionPromotionGroupVOList.stream().filter(dp -> dp.getActivityType() != null &&
                    dp.getActivityType() == 4).mapToInt(DistributionPromotionGroupVO :: getCount).sum();
            distributionPromotionStatVO.setMoments(moments);
            distributionPromotionStatVO.setPoster(poster);
            distributionPromotionStatVO.setSpecialSubject(specialSubject);
            distributionPromotionStatVO.setProduct(product);
            distributionPromotionStatVO.setTotal(total);
        }
        return distributionPromotionStatVO;
    }

    @Override
    public PageVO<PurchaseRankingDTO> pagePurchaseRanking(PageDTO pageDTO, DistributionPurchaseRecordDTO dto) {
        PageVO<PurchaseRankingDTO> rankingPageVO = PageUtil.doPage(pageDTO, () -> distributionPurchaseRecordMapper.pagePurchaseRanking(dto));
        if (CollectionUtils.isEmpty(rankingPageVO.getList())){
            return new PageVO<>();
        }
        rankingPageVO.getList().forEach(purchaseRankingDTO -> {
            ServerResponseEntity<SpuVO> spuData = spuFeignClient.getSpuAndSkuBySpuId(purchaseRankingDTO.getSpuId());
            if (spuData.isSuccess() && null != spuData.getData()){
                purchaseRankingDTO.setSpuName(spuData.getData().getName());
                purchaseRankingDTO.setMainImgUrl(spuData.getData().getMainImgUrl());
                if (CollectionUtils.isNotEmpty(spuData.getData().getSkus())){
                    purchaseRankingDTO.setMinPrice(spuData.getData().getSkus().stream().mapToLong(SkuVO::getPriceFee).min().getAsLong());
                    purchaseRankingDTO.setMaxPrice(spuData.getData().getSkus().stream().mapToLong(SkuVO::getPriceFee).max().getAsLong());
                }
            }
        });
        return rankingPageVO;
    }
}
