package com.mall4j.cloud.biz.model.cp;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 消息通知表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Notice extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 内容
     */
    private String content;

    /**
     * 推送时间
     */
    private String pushTime;

    /**
     * 推送对象
     */
    private String pushObj;
    /**
     * 消息url
     */
    private String msgUrl;
    /**
     * 消息类型 1好友流失，2跟进提醒，3素材浏览提醒，4敏感词命中，5超时回复
     */
    private String msgType;

}
