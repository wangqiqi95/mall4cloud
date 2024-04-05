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
import com.mall4j.cloud.distribution.dto.DistributionBrowseRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionShareRecordDTO;
import com.mall4j.cloud.distribution.mapper.DistributionBrowseRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionBrowseRecord;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import com.mall4j.cloud.distribution.service.DistributionBrowseRecordService;
import com.mall4j.cloud.distribution.service.DistributionUnreadRecordService;
import com.mall4j.cloud.distribution.vo.DistributionBrowseRecordVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionGroupVO;
import com.mall4j.cloud.distribution.vo.DistributionPromotionStatVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 分享推广-浏览记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Service
public class DistributionBrowseRecordServiceImpl implements DistributionBrowseRecordService {

    @Autowired
    private DistributionBrowseRecordMapper distributionBrowseRecordMapper;

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
    public PageVO<DistributionBrowseRecordVO> page(PageDTO pageDTO, DistributionBrowseRecordDTO dto) {
        DistributionUnreadRecord unreadRecord = distributionUnreadRecordService.getByUser(dto.getShareType(), dto.getShareId());
        if (null != unreadRecord){
            unreadRecord.setUnreadBrowseNum(0);
            distributionUnreadRecordService.update(unreadRecord);
        }
        if (StringUtils.isNotEmpty(dto.getKeywords())){
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(dto.getKeywords());
            if (userData.isSuccess() && null != userData.getData()){
                dto.setBrowseId(userData.getData().getUserId());
            } else {
                dto.setBrowseId(-1L);
            }
        }
        PageVO<DistributionBrowseRecordVO> page = PageUtil.doPage(pageDTO, () -> distributionBrowseRecordMapper.list(dto));
        if (CollectionUtils.isEmpty(page.getList())){
            return page;
        }
        page.getList().forEach(distributionBrowseRecordVO -> {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(distributionBrowseRecordVO.getBrowseId());
            if (userData.isSuccess() && null != userData.getData()){
                distributionBrowseRecordVO.setBrowseMobile(userData.getData().getUserMobile());
                distributionBrowseRecordVO.setBrowseName(userData.getData().getNickName());
            }
        });
        return page;
    }

    @Override
    public DistributionBrowseRecord getById(Long id) {
        return distributionBrowseRecordMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionBrowseRecord distributionBrowseRecord) {

        if (StringUtils.isNotEmpty(distributionBrowseRecord.getTentacleNo())) {
            ServerResponseEntity<TentacleContentVO> responseEntity = tentacleContentFeignClient.findByTentacleNo(distributionBrowseRecord.getTentacleNo());
            if (responseEntity.isSuccess() && null != responseEntity.getData() && null != responseEntity.getData().getTentacle()) {
                if (responseEntity.getData().getTentacle().getTentacleType() == 4){
                    distributionBrowseRecord.setShareType(1);
                    distributionBrowseRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                } else if (responseEntity.getData().getTentacle().getTentacleType() == 3){
                    distributionBrowseRecord.setShareType(2);
                    distributionBrowseRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                } else if (responseEntity.getData().getTentacle().getTentacleType() == 2){
                    distributionBrowseRecord.setShareType(3);
                    distributionBrowseRecord.setShareId(responseEntity.getData().getTentacle().getBusinessId());
                }
            }
        }
        distributionBrowseRecordMapper.save(distributionBrowseRecord);

        saveActivityShareRecord(distributionBrowseRecord);

        saveGuideShareRecord(distributionBrowseRecord);

        if (null != distributionBrowseRecord.getShareId()){
            DistributionUnreadRecord record = new DistributionUnreadRecord();
            record.setUserId(distributionBrowseRecord.getShareId());
            record.setIdentityType(distributionBrowseRecord.getShareType());
            record.setUnreadBrowseNum(1);
            sendDistributionGuideShareTemplate.syncSend(record);
        }
    }


    private void saveActivityShareRecord(DistributionBrowseRecord distributionBrowseRecord) {
        DistributionActivityShareRecordDTO dto = new DistributionActivityShareRecordDTO();
        dto.setActivityId(distributionBrowseRecord.getActivityId());
        dto.setActivityType(distributionBrowseRecord.getActivityType());
        dto.setActivityName(distributionBrowseRecord.getActivityName());
        dto.setRecordType(2);
        dto.setExist(Optional.ofNullable(distributionBrowseRecordMapper.countByBrowseAndActivity(distributionBrowseRecord.getBrowseId(), distributionBrowseRecord.getBrowseType(), distributionBrowseRecord.getActivityId(), distributionBrowseRecord.getActivityType())).orElse(0) != 1);
        sendDistributionActivityShareTemplate.syncSend(dto);
    }


    private void saveGuideShareRecord(DistributionBrowseRecord distributionBrowseRecord) {
        if (null == distributionBrowseRecord.getShareId()){
            return;
        }
        if (null == distributionBrowseRecord.getShareType()){
            return;
        }
        if (1 != distributionBrowseRecord.getShareType()){
            return;
        }
        DistributionShareRecordDTO dto = new DistributionShareRecordDTO();
        dto.setActivityId(distributionBrowseRecord.getActivityId());
        dto.setActivityType(distributionBrowseRecord.getActivityType());
        dto.setRecordType(2);
        dto.setExist(Optional.ofNullable(distributionBrowseRecordMapper.countByBrowseAndActivity(distributionBrowseRecord.getBrowseId(), distributionBrowseRecord.getBrowseType(), distributionBrowseRecord.getActivityId(), distributionBrowseRecord.getActivityType())).orElse(0) != 1);
        dto.setShareId(distributionBrowseRecord.getShareId());
        sendDistributionGuideShareTemplate.syncSend(dto);
    }


    @Override
    public void update(DistributionBrowseRecord distributionBrowseRecord) {
        distributionBrowseRecordMapper.update(distributionBrowseRecord);
    }


    @Override
    public void deleteById(Long id) {
        distributionBrowseRecordMapper.deleteById(id);
    }

    @Override
    public Integer countNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBrowseRecordMapper.countNumByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public Integer countNumByActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBrowseRecordMapper.countNumByShareActivityAndDate(shareId, activityId, activityType, startDate, endDate);
    }

    @Override
    public Integer countUserNumByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBrowseRecordMapper.countUserNumByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public Integer countUserNumByActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBrowseRecordMapper.countUserNumByShareActivityAndDate(shareId, activityId, activityType, startDate, endDate);
    }

    @Override
    public DistributionPromotionStatVO stat(Integer shareType, Long shareId) {
        DistributionPromotionStatVO distributionPromotionStatVO = new DistributionPromotionStatVO();
        List<DistributionPromotionGroupVO> distributionPromotionGroupVOList =  distributionBrowseRecordMapper.groupByActivityType(shareType,
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
    public List<DistributionBrowseRecordVO> listStaffByActivity(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionBrowseRecordMapper.listStaffByActivity(activityId, activityType, startDate, endDate);
    }
}
