package com.mall4j.cloud.group.strategy.ad.attachment;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.cache.constant.GroupCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import com.mall4j.cloud.group.vo.PopUpAdFormImageVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ImageAttachmentHandler implements AttachmentHandler {
    @Override
    public void extraction(PopUpAdContainerVO container, AttachmentBO attachmentBO) {

        //获取所有的图片
        List<PopUpAdFormImageVO> popUpAdFormImageVOList = attachmentBO.getPopUpAdAttachmentVOList().stream()
                .map(adAttachmentVO -> {
                    PopUpAdFormImageVO pop = new PopUpAdFormImageVO();

                    pop.setPopUpAdID(attachmentBO.getPopUpAdId());
                    //素材类型
                    pop.setAttachmentType(attachmentBO.getAttachmentType());
                    //图片URL
                    pop.setPicUrl(adAttachmentVO.getMediaUrl());
                    //页面LINK
                    pop.setLink(adAttachmentVO.getLink());
                    //页面LINK类型
                    pop.setLinkType(adAttachmentVO.getLinkType());
                    pop.setAutoOffSeconds(attachmentBO.getAutoOffSeconds());

                    return pop;
                })
                .collect(Collectors.toList());

        //保存数据到缓存
        if (CollectionUtil.isNotEmpty(popUpAdFormImageVOList)){
            Integer expires = DateUtils.getTheBetweenSeconds(LocalDateTime.now(), attachmentBO.getActivityEndTime());
            RedisUtil.set(GroupCacheNames.ATTACHMENT+attachmentBO.getPopUpAdId(),popUpAdFormImageVOList,expires);
            container.getPopUpAdFormImageVOList().addAll(popUpAdFormImageVOList);
        }



    }
}
