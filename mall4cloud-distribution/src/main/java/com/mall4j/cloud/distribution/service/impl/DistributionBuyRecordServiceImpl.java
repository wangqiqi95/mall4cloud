package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionBuyRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionShareRecordDTO;
import com.mall4j.cloud.distribution.mapper.DistributionBuyRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionBuyRecord;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import com.mall4j.cloud.distribution.service.DistributionBuyRecordService;
import com.mall4j.cloud.distribution.service.DistributionUnreadRecordService;
import com.mall4j.cloud.distribution.vo.DistributionBuyRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionGroupVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-下单记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Service
public class DistributionBuyRecordServiceImpl implements DistributionBuyRecordService {

    @Autowired
    private DistributionBuyRecordMapper distributionBuyRecordMapper;

    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;

    @Autowired
    private OnsMQTemplate sendDistributionActivityShareTemplate;

    @Autowired
    private OnsMQTemplate sendDistributionGuideShareTemplate;

    @Autowired
    private DistributionUnreadRecordService distributionUnreadRecordService;

    @Autowired
    private UserFeignClient userFeignClient;


    @Override
    public PageVO<DistributionBuyRecordVO> page(PageDTO pageDTO, DistributionBuyRecordDTO dto) {
        DistributionUnreadRecord unreadRecord = distributionUnreadRecordService.getByUser(dto.getShareType(), dto.getShareId());
        if (null != unreadRecord){
            unreadRecord.setUnreadBuyNum(0);
            distributionUnreadRecordService.update(unreadRecord);
        }
        if (StringUtils.isNotEmpty(dto.getKeywords())){
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(dto.getKeywords());
            if (userData.isSuccess() && null != userData.getData()){
                dto.setBuyId(userData.getData().getUserId());
            } else {
                dto.setBuyId(-1L);
            }
        }
        PageVO<DistributionBuyRecordVO> page = PageUtil.doPage(pageDTO, () -> distributionBuyRecordMapper.list(dto));
        if (CollectionUtils.isEmpty(page.getList())){
            return page;
        }
        page.getList().forEach(distributionBuyRecordVO -> {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(distributionBuyRecordVO.getBuyId());
            if (userData.isSuccess() && null != userData.getData()){
                distributionBuyRecordVO.setBuyMobile(userData.getData().getUserMobile());
                distributionBuyRecordVO.setBuyName(userData.getData().getNickName());
            }
        });
        return page;
    }

    @Override
    public DistributionBuyRecord getById(Long id) {
        return distributionBuyRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionBuyRecord distributionBuyRecord) {

        if (StringUtils.isNotEmpty(distributionBuyRecord.getTentacleNo())) {
            ServerResponseEntity<TentacleContentVO> responseEntity = tentacleContentFeignClient.findByTentacleNo(distributionBuyRecord.getTentacleNo());
            if (responseEntity.isSuccess() && null != responseEntity.getData() && null != responseEntity.getData().getTentacle()) {
                if (responseEntity.getData().getTentacle().getTentacleType() == 4){
                    distributionBuyRecord.setShareType(1);
                    distributionBuyRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                } else if (responseEntity.getData().getTentacle().getTentacleType() == 3){
                    distributionBuyRecord.setShareType(2);
                    distributionBuyRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                } else if (responseEntity.getData().getTentacle().getTentacleType() == 2){
                    distributionBuyRecord.setShareType(3);
                    distributionBuyRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                }
            }
        }
        distributionBuyRecordMapper.save(distributionBuyRecord);

        saveActivityShareRecord(distributionBuyRecord);

        saveGuideShareRecord(distributionBuyRecord);

        if (null != distributionBuyRecord.getShareId()){
            DistributionUnreadRecord record = new DistributionUnreadRecord();
            record.setUserId(distributionBuyRecord.getShareId());
            record.setIdentityType(distributionBuyRecord.getShareType());
            record.setUnreadBuyNum(1);
            sendDistributionGuideShareTemplate.syncSend(record);
        }

    }

    private void saveGuideShareRecord(DistributionBuyRecord distributionBuyRecord) {
        if (null == distributionBuyRecord.getShareId()){
            return;
        }
        if (null == distributionBuyRecord.getShareType()){
            return;
        }
        if (1 != distributionBuyRecord.getShareType()){
            return;
        }
        DistributionShareRecordDTO dto = new DistributionShareRecordDTO();
        dto.setActivityId(distributionBuyRecord.getActivityId());
        dto.setActivityType(distributionBuyRecord.getActivityType());
        dto.setRecordType(4);
        dto.setExist(distributionBuyRecordMapper.getByBuyAndActivity(distributionBuyRecord.getBuyId(), distributionBuyRecord.getBuyType(), distributionBuyRecord.getActivityId(), distributionBuyRecord.getActivityType()) != null);
        dto.setShareId(distributionBuyRecord.getShareId());
        sendDistributionGuideShareTemplate.syncSend(dto);
    }

    private void saveActivityShareRecord(DistributionBuyRecord distributionBuyRecord) {
        DistributionActivityShareRecordDTO dto = new DistributionActivityShareRecordDTO();
        if (distributionBuyRecord.getActivityType() == 4){
            dto.setActivityId(distributionBuyRecord.getProductId());
            dto.setActivityName(distributionBuyRecord.getProductName());
        } else {
            dto.setActivityId(distributionBuyRecord.getActivityId());
            dto.setActivityName(distributionBuyRecord.getActivityName());
        }
        dto.setRecordType(4);
        dto.setExist(distributionBuyRecordMapper.getByBuyAndActivity(distributionBuyRecord.getBuyId(), distributionBuyRecord.getBuyType(), distributionBuyRecord.getActivityId(), distributionBuyRecord.getActivityType()) != null);
        sendDistributionActivityShareTemplate.syncSend(dto);
    }

    @Override
    public void update(DistributionBuyRecord distributionBuyRecord) {
        distributionBuyRecordMapper.update(distributionBuyRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionBuyRecordMapper.deleteById(id);
    }

    @Override
    public int countNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBuyRecordMapper.countNumByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public int countUserNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBuyRecordMapper.countUserNumByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public DistributionPromotionStatVO stat(Integer shareType, Long shareId) {
        DistributionPromotionStatVO distributionPromotionStatVO = new DistributionPromotionStatVO();
        List<DistributionPromotionGroupVO> distributionPromotionGroupVOList =  distributionBuyRecordMapper.groupByActivityType(shareType,
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
}
