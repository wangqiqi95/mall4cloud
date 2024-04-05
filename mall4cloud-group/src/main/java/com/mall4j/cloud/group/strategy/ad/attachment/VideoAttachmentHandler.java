package com.mall4j.cloud.group.strategy.ad.attachment;

import com.mall4j.cloud.common.cache.constant.GroupCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import com.mall4j.cloud.group.vo.PopUpAdFormVideoVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class VideoAttachmentHandler implements AttachmentHandler {
    @Override
    public void extraction(PopUpAdContainerVO container, AttachmentBO attachmentBO) {

        //获取视频
        PopUpAdAttachmentVO popUpAdAttachmentVO = attachmentBO.getPopUpAdAttachmentVOList().stream()
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(popUpAdAttachmentVO.getMediaUrl())){
            PopUpAdFormVideoVO pop = new PopUpAdFormVideoVO();

            pop.setPopUpAdID(attachmentBO.getPopUpAdId());
            pop.setAttachmentType(attachmentBO.getAttachmentType());
            pop.setVideoUrl(popUpAdAttachmentVO.getMediaUrl());
            pop.setAutoOffSeconds(attachmentBO.getAutoOffSeconds());

            //保存数据到缓存
            Integer expires = DateUtils.getTheBetweenSeconds(LocalDateTime.now(), attachmentBO.getActivityEndTime());
            RedisUtil.set(GroupCacheNames.ATTACHMENT+attachmentBO.getPopUpAdId(),pop,expires);

            container.getPopUpAdFormVideoList().add(pop);
        }

    }
}
