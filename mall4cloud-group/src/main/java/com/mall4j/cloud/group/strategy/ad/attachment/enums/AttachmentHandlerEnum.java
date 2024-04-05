package com.mall4j.cloud.group.strategy.ad.attachment.enums;

import com.mall4j.cloud.group.strategy.ad.attachment.CouponAttachmentHandler;
import com.mall4j.cloud.group.strategy.ad.attachment.ImageAttachmentHandler;
import com.mall4j.cloud.group.strategy.ad.attachment.QuestionnaireHandler;
import com.mall4j.cloud.group.strategy.ad.attachment.VideoAttachmentHandler;
import lombok.Getter;

@Getter
public enum AttachmentHandlerEnum {

    IMAGE("IMAGE", ImageAttachmentHandler.class),
    VIDEO("VIDEO", VideoAttachmentHandler.class),
    COUPON("COUPON", CouponAttachmentHandler.class),
    QUESTIONNAIRE("QUESTIONNAIRE", QuestionnaireHandler.class);//QuestionnaireUESTIONNAIRE


    private String contentType;

    private Class handler;

    AttachmentHandlerEnum(String contentType, Class handler){
        this.contentType = contentType;

        this.handler = handler;
    }

}
