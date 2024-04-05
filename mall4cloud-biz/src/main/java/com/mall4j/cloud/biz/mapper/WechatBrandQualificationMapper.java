package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WechatBrandQualificationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * The Table wechat_brand_qualification.
 * wechat_brand_qualification
 */
public interface WechatBrandQualificationMapper{

    /**
     * desc:insert:wechat_brand_qualification.<br/>
     * descSql =  SELECT LAST_INSERT_ID() INSERT INTO wechat_brand_qualification( ID ,TENANT_ID ,BRAND_ID ,WX_BRAND_ID ,LICENSE ,BRAND_AUDIT_TYPE ,TRADEMARK_TYPE ,BRAND_MANAGEMENT_TYPE ,COMMODITY_ORIGIN_TYPE ,BRAND_WORDING ,SALE_AUTHORIZATION ,TRADEMARK_REGISTRATION_CERTIFICATE ,TRADEMARK_REGISTRANT ,TRADEMARK_REGISTRANT_NU ,TRADEMARK_AUTHORIZATION_PERIOD ,TRADEMARK_REGISTRATION_APPLICATION ,TRADEMARK_APPLICANT ,TRADEMARK_APPLICATION_TIME ,AUDIT_ID ,AUDIT_TIME ,STATUS ,CREATE_BY ,CREATE_TIME ,UPDATE_BY ,UPDATE_TIME ,IS_DELETED )VALUES( #{id,jdbcType=BIGINT} , #{tenantId,jdbcType=VARCHAR} , #{brandId,jdbcType=VARCHAR} , #{wxBrandId,jdbcType=VARCHAR} , #{license,jdbcType=VARCHAR} , #{brandAuditType,jdbcType=TINYINT} , #{trademarkType,jdbcType=VARCHAR} , #{brandManagementType,jdbcType=TINYINT} , #{commodityOriginType,jdbcType=TINYINT} , #{brandWording,jdbcType=VARCHAR} , #{saleAuthorization,jdbcType=VARCHAR} , #{trademarkRegistrationCertificate,jdbcType=VARCHAR} , #{trademarkRegistrant,jdbcType=VARCHAR} , #{trademarkRegistrantNu,jdbcType=VARCHAR} , #{trademarkAuthorizationPeriod,jdbcType=TIMESTAMP} , #{trademarkRegistrationApplication,jdbcType=VARCHAR} , #{trademarkApplicant,jdbcType=VARCHAR} , #{trademarkApplicationTime,jdbcType=TIMESTAMP} , #{auditId,jdbcType=VARCHAR} , #{auditTime,jdbcType=TIMESTAMP} , #{status,jdbcType=TINYINT} , #{createBy,jdbcType=VARCHAR} , #{createTime,jdbcType=TIMESTAMP} , #{updateBy,jdbcType=VARCHAR} , #{updateTime,jdbcType=TIMESTAMP} , 'N' )
     * @param entity entity
     * @return Long
     */
    Long insert(WechatBrandQualificationDO entity);
    /**
     * desc:update table:wechat_brand_qualification.<br/>
     * descSql =  UPDATE wechat_brand_qualification SET TENANT_ID = #{tenantId,jdbcType=VARCHAR} ,BRAND_ID = #{brandId,jdbcType=VARCHAR} ,WX_BRAND_ID = #{wxBrandId,jdbcType=VARCHAR} ,LICENSE = #{license,jdbcType=VARCHAR} ,BRAND_AUDIT_TYPE = #{brandAuditType,jdbcType=TINYINT} ,TRADEMARK_TYPE = #{trademarkType,jdbcType=VARCHAR} ,BRAND_MANAGEMENT_TYPE = #{brandManagementType,jdbcType=TINYINT} ,COMMODITY_ORIGIN_TYPE = #{commodityOriginType,jdbcType=TINYINT} ,BRAND_WORDING = #{brandWording,jdbcType=VARCHAR} ,SALE_AUTHORIZATION = #{saleAuthorization,jdbcType=VARCHAR} ,TRADEMARK_REGISTRATION_CERTIFICATE = #{trademarkRegistrationCertificate,jdbcType=VARCHAR} ,TRADEMARK_REGISTRANT = #{trademarkRegistrant,jdbcType=VARCHAR} ,TRADEMARK_REGISTRANT_NU = #{trademarkRegistrantNu,jdbcType=VARCHAR} ,TRADEMARK_AUTHORIZATION_PERIOD = #{trademarkAuthorizationPeriod,jdbcType=TIMESTAMP} ,TRADEMARK_REGISTRATION_APPLICATION = #{trademarkRegistrationApplication,jdbcType=VARCHAR} ,TRADEMARK_APPLICANT = #{trademarkApplicant,jdbcType=VARCHAR} ,TRADEMARK_APPLICATION_TIME = #{trademarkApplicationTime,jdbcType=TIMESTAMP} ,AUDIT_ID = #{auditId,jdbcType=VARCHAR} ,AUDIT_TIME = #{auditTime,jdbcType=TIMESTAMP} ,STATUS = #{status,jdbcType=TINYINT} ,CREATE_BY = #{createBy,jdbcType=VARCHAR} ,CREATE_TIME = #{createTime,jdbcType=TIMESTAMP} ,UPDATE_BY = #{updateBy,jdbcType=VARCHAR} ,UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP} ,IS_DELETED = #{isDeleted,jdbcType=INTEGER} WHERE ID = #{id,jdbcType=BIGINT}
     * @param entity entity
     * @return Long
     */
    Long update(WechatBrandQualificationDO entity);
    /**
     * desc:delete:wechat_brand_qualification.<br/>
     * descSql =  DELETE FROM wechat_brand_qualification WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return Long
     */
    Long deleteByPrimary(Long id);
    /**
     * desc:get:wechat_brand_qualification.<br/>
     * descSql =  SELECT * FROM wechat_brand_qualification WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return WechatBrandQualificationDO
     */
    WechatBrandQualificationDO getByPrimary(Long id);

    List<WechatBrandQualificationDO> list();

    WechatBrandQualificationDO getByAuditId(String auditId);

    Integer syncAuditResult(@Param("auditId") String auditId, @Param("auditContent") String auditContent, @Param("status") Integer status,@Param("wxBrandId") Long wxBrandId);
}
