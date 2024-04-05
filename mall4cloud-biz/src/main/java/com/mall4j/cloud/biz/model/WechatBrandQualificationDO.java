package com.mall4j.cloud.biz.model;

import lombok.Data;

import java.util.Date;

/**
 * The table wechat_brand_qualification
 */
@Data
public class WechatBrandQualificationDO{

    /**
     * id 主键ID.
     */
    private Long id;
    /**
     * tenantId 租户ID.
     */
    private String tenantId;
    /**
     * brandId 品牌ID.
     */
    private String brandId;
    /**
     * wxBrandId 微信类目ID.
     */
    private String wxBrandId;
    /**
     * license 营业执照或组织机构代码证，图片URL.
     */
    private String license;
    /**
     * brandAuditType 1-国内品牌申请-R标 2-国内品牌申请-TM标 3-海外品牌申请-R标 4-海外品牌申请-TM标.
     */
    private Integer brandAuditType;
    /**
     * trademarkType 商标分类 第X类.
     */
    private String trademarkType;
    /**
     * brandManagementType 1-自有品牌 2-代理品牌 3-无品牌.
     */
    private Integer brandManagementType;
    /**
     * commodityOriginType 商品产地是否进口 1-是 2-否.
     */
    private Integer commodityOriginType;
    /**
     * brandWording 商标/品牌词.
     */
    private String brandWording;
    /**
     * saleAuthorization 销售授权书（如商持人为自然人，还需提供有其签名的身份证正反面扫描件)，图片URL, 代理品牌必填.
     */
    private String saleAuthorization;
    /**
     * trademarkRegistrationCertificate 商标注册证书，图片URL, R标必填.
     */
    private String trademarkRegistrationCertificate;
    /**
     * trademarkRegistrant 商标注册人姓名 R标必填.
     */
    private String trademarkRegistrant;
    /**
     * trademarkRegistrantNu 商标注册号/申请号.
     */
    private String trademarkRegistrantNu;
    /**
     * trademarkAuthorizationPeriod 商标有效期，YYYY-MM-DD HH:MM:SS R标必填.
     */
    private Date trademarkAuthorizationPeriod;
    /**
     * trademarkRegistrationApplication 商标注册申请受理通知书，图片URL TM标必填.
     */
    private String trademarkRegistrationApplication;
    /**
     * trademarkApplicant 商标注册申请受理通知书，图片URL TM标必填.
     */
    private String trademarkApplicant;
    /**
     * trademarkApplicationTime 商标申请时间, YYYY-MM-DD HH:MM:SS TM标必填.
     */
    private Date trademarkApplicationTime;
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
     * status 状态,0:草稿,1:审核通过, 2:审核中, 9:审核拒绝.
     */
    private Integer status;
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
