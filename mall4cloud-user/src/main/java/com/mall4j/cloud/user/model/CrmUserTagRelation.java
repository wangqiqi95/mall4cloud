package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 数云维护 用户标签关系表
 *
 * @author FrozenWatermelon
 * @date 2023-11-25 10:41:27
 */
@Data
public class CrmUserTagRelation extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 目录id
     */
    private String categoryid;

    /**
     * 标签id
     */
    private String tagid;

    /**
     * 标签名称
     */
    private String tagname;

    /**
     * 标签值
     */
    private String tagvalue;

    /**
     * 原始用户id
     */
    private String originid;

    /**
     * 微信unionId
     */
    private String unionid;

    /**
     * 是否有效Y/N
     */
    private String enable;

    private String key;

}
