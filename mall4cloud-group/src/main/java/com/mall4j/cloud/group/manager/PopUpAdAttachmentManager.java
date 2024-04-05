package com.mall4j.cloud.group.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.group.bo.AddPopUpAdAttachmentBO;
import com.mall4j.cloud.group.dto.AddPopUpAdAttachmentDTO;
import com.mall4j.cloud.group.dto.UpdatePopUpAdAttachmentDTO;
import com.mall4j.cloud.group.mapper.PopUpAdAttachmentMapper;
import com.mall4j.cloud.group.model.PopUpAdAttachment;
import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class PopUpAdAttachmentManager {

    @Autowired
    private PopUpAdAttachmentMapper popUpAdAttachmentMapper;

    @Autowired
    private MapperFacade mapperFacade;


    public List<PopUpAdAttachmentVO> getPopUpAdAttachmentByAdIdList(List<Long> adIdList){

        List<PopUpAdAttachment> popUpAdAttachments = popUpAdAttachmentMapper.selectList(
                new LambdaQueryWrapper<PopUpAdAttachment>()
                        .in(PopUpAdAttachment::getPopUpAdId, adIdList)
        );


        List<PopUpAdAttachmentVO> popUpAdAttachmentVOList = mapperFacade.mapAsList(popUpAdAttachments, PopUpAdAttachmentVO.class);

        return popUpAdAttachmentVOList;

    }

    public List<PopUpAdAttachmentVO> getPopUpAdAttachmentByAdId(Long adId){

        List<PopUpAdAttachment> popUpAdAttachments = popUpAdAttachmentMapper.selectList(
                new LambdaQueryWrapper<PopUpAdAttachment>()
                        .eq(PopUpAdAttachment::getPopUpAdId, adId)
        );


        List<PopUpAdAttachmentVO> popUpAdAttachmentVOList = mapperFacade.mapAsList(popUpAdAttachments, PopUpAdAttachmentVO.class);

        return popUpAdAttachmentVOList;

    }

    public void addBatch(List<AddPopUpAdAttachmentBO> addPopUpAdAttachmentBOS, Long adId, Long createUser){

        List<PopUpAdAttachment> popUpAdAttachments = mapperFacade.mapAsList(addPopUpAdAttachmentBOS, PopUpAdAttachment.class);

        popUpAdAttachments.stream().forEach(popUpAdAttachment -> {
                popUpAdAttachment.setPopUpAdId(adId);
                popUpAdAttachment.setCreateUser(createUser);
                popUpAdAttachmentMapper.insert(popUpAdAttachment);
        });

    }


    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByIdList(List<Long> attachmentIdList){

        popUpAdAttachmentMapper.deleteBatchIds(attachmentIdList);

    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByAdId(Long adId){
        popUpAdAttachmentMapper.delete(
                new LambdaQueryWrapper<PopUpAdAttachment>().eq(PopUpAdAttachment::getPopUpAdId, adId)
        );

    }


    @Transactional(rollbackFor = Exception.class)
    public void compareUpdate(PopUpAdAttachmentVO adAttachment, UpdatePopUpAdAttachmentDTO update, Long userId){

        boolean editFlag = false;

        LambdaUpdateWrapper<PopUpAdAttachment> updateWrapper = new LambdaUpdateWrapper<>();
        if (StringUtils.isNotEmpty(adAttachment.getMediaUrl())
                && !adAttachment.getMediaUrl().equals(update.getMediaUrl())){
            editFlag = true;

            updateWrapper
                    .set(PopUpAdAttachment::getMediaUrl, update.getMediaUrl());

        }
        if (StringUtils.isNotEmpty(adAttachment.getLink())
                && !adAttachment.getLink().equals(update.getLink())){
            editFlag = true;

            updateWrapper
                    .set(PopUpAdAttachment::getLink, update.getLink());

        }
        if (StringUtils.isNotEmpty(adAttachment.getLinkType())
                && !adAttachment.getLinkType().equals(update.getLinkType())){
            editFlag = true;

            updateWrapper
                    .set(PopUpAdAttachment::getLinkType, update.getLinkType());

        }
        if (Objects.nonNull(adAttachment.getBusinessId())
                && !adAttachment.getBusinessId().equals(update.getBusinessId())){
            editFlag = true;

            updateWrapper
                    .set(PopUpAdAttachment::getBusinessId, update.getBusinessId());
        }
        if (editFlag){
            popUpAdAttachmentMapper.update(null,
                    updateWrapper
                            .set(PopUpAdAttachment::getUpdateTime, LocalDateTime.now())
                            .set(PopUpAdAttachment::getUpdateUser, userId)
                            .eq(PopUpAdAttachment::getPopUpAdMediaId, adAttachment.getPopUpAdMediaId()));
        }

    }
}
