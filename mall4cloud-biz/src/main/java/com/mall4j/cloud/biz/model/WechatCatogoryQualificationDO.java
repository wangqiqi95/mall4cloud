package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.util.Date;

/**
 * The table wechat_catogory_qualification
 */
@Data
public class WechatCatogoryQualificationDO{

    /**
     * id 主键ID.
     */
    private Long id;
    /**
     * tenantId 租户ID.
     */
    private String tenantId;
    /**
     * categoryId 类目ID.
     */
    private String categoryId;
    /**
     * wxCategoryId 微信类目ID.
     */
    private String wxCategoryId;
    /**
     * wxCategoryName 微信类目名称.
     */
    private String wxCategoryName;
    /**
     * wxQualification 微信类目资质.
     */
    private String wxQualification;
    /**
     * wxQualificationType 微信类目资质类型,0:不需要,1:必填,2:选填.
     */
    private Integer wxQualificationType;
    /**
     * wxProductQualification 微信商品资质.
     */
    private String wxProductQualification;
    /**
     * wxProductQualificationType 微信商品资质类型,0:不需要,1:必填,2:选填.
     */
    private Integer wxProductQualificationType;
    /**
     * wxSecondCatId 微信二级类目ID.
     */
    private String wxSecondCatId;
    /**
     * wxSecondCatName 微信二级类目名称.
     */
    private String wxSecondCatName;
    /**
     * wxFirstCatId 微信一级类目ID.
     */
    private String wxFirstCatId;
    /**
     * wxFirstCatName 微信一级类目名称.
     */
    private String wxFirstCatName;
    /**
     * status 状态,0:草稿,1:审核通过, 2:审核中, 9:审核拒绝.
     */
    private Integer status;
    /**
     * auditId 审核ID.
     */
    private String auditId;
    /**
     * 审核内容
     */
    private String auditContent;
    /**
     * auditTime 提交审核时间.
     */
    private Date auditTime;
    /**
     * qualificationUrls 类目资质URL列表,逗号隔开.
     */
    private String qualificationUrls;
    /**
     * createBy 创建人 USERID.
     */
    private String createBy;
    /**
     * createTime 创建时间.
     */
    private Date createTime;
    /**
     * updateBy 修改人.
     */
    private String updateBy;
    /**
     * updateTime 修改时间.
     */
    private Date updateTime;
    /**
     * isDeleted 是否删除 0 否 1是.
     */
    private Integer isDeleted;

}
