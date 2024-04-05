package com.mall4j.cloud.group.strategy.ad.attachment;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.common.util.SpringContextUtils;
import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.strategy.ad.attachment.enums.AttachmentHandlerEnum;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseAttachmentHandler {

    public PopUpAdContainerVO extraction(List<AttachmentBO> attachmentBOList){

        PopUpAdContainerVO container = new PopUpAdContainerVO();

        container.setPopUpAdFormVideoList(new ArrayList<>());

        container.setPopUpAdFormImageVOList(new ArrayList<>());
        container.setPopUpAdFormCouponList(new ArrayList<>());

        container.setPopUpAdFormQuestionnaireVOList(new ArrayList<>());

        if (CollectionUtil.isNotEmpty(attachmentBOList)){
            //提取相关附件
            attachmentBOList.stream()
                    .forEach(attachmentBO ->
                            //获取相关处理器单例bean，并提取相关附件
                            this.getHandler(attachmentBO.getAttachmentType()).extraction(container, attachmentBO)
                    );
        }

        return container;
    }


    private AttachmentHandler getHandler(String attachmentType){
        Class handler = AttachmentHandlerEnum.valueOf(attachmentType).getHandler();
        AttachmentHandler attachmentHandler = (AttachmentHandler)SpringContextUtils.getBean(handler);
        return attachmentHandler;
    }
}
