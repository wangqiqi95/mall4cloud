package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WechatCatogoryQualificationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * The Table wechat_catogory_qualification.
 * wechat_catogory_qualification
 */
public interface WechatCatogoryQualificationMapper{

    /**
     * desc:insert:wechat_catogory_qualification.<br/>
     * descSql =  SELECT LAST_INSERT_ID() INSERT INTO wechat_catogory_qualification( ID ,TENANT_ID ,CATEGORY_ID ,WX_CATEGORY_ID ,WX_CATEGORY_NAME ,WX_QUALIFICATION ,WX_QUALIFICATION_TYPE ,WX_PRODUCT_QUALIFICATION ,WX_PRODUCT_QUALIFICATION_TYPE ,WX_SECOND_CAT_ID ,WX_SECOND_CAT_NAME ,WX_FIRST_CAT_ID ,WX_FIRST_CAT_NAME ,STATUS ,AUDIT_ID ,AUDIT_TIME ,QUALIFICATION_URLS ,CREATE_BY ,CREATE_TIME ,UPDATE_BY ,UPDATE_TIME ,IS_DELETED )VALUES( #{id,jdbcType=BIGINT} , #{tenantId,jdbcType=VARCHAR} , #{categoryId,jdbcType=VARCHAR} , #{wxCategoryId,jdbcType=VARCHAR} , #{wxCategoryName,jdbcType=VARCHAR} , #{wxQualification,jdbcType=VARCHAR} , #{wxQualificationType,jdbcType=TINYINT} , #{wxProductQualification,jdbcType=VARCHAR} , #{wxProductQualificationType,jdbcType=TINYINT} , #{wxSecondCatId,jdbcType=VARCHAR} , #{wxSecondCatName,jdbcType=VARCHAR} , #{wxFirstCatId,jdbcType=VARCHAR} , #{wxFirstCatName,jdbcType=VARCHAR} , #{status,jdbcType=TINYINT} , #{auditId,jdbcType=VARCHAR} , #{auditTime,jdbcType=TIMESTAMP} , #{qualificationUrls,jdbcType=VARCHAR} , #{createBy,jdbcType=VARCHAR} , #{createTime,jdbcType=TIMESTAMP} , #{updateBy,jdbcType=VARCHAR} , #{updateTime,jdbcType=TIMESTAMP} , 'N' )
     * @param entity entity
     * @return Long
     */
    Long insert(WechatCatogoryQualificationDO entity);
    /**
     * desc:update table:wechat_catogory_qualification.<br/>
     * descSql =  UPDATE wechat_catogory_qualification SET TENANT_ID = #{tenantId,jdbcType=VARCHAR} ,CATEGORY_ID = #{categoryId,jdbcType=VARCHAR} ,WX_CATEGORY_ID = #{wxCategoryId,jdbcType=VARCHAR} ,WX_CATEGORY_NAME = #{wxCategoryName,jdbcType=VARCHAR} ,WX_QUALIFICATION = #{wxQualification,jdbcType=VARCHAR} ,WX_QUALIFICATION_TYPE = #{wxQualificationType,jdbcType=TINYINT} ,WX_PRODUCT_QUALIFICATION = #{wxProductQualification,jdbcType=VARCHAR} ,WX_PRODUCT_QUALIFICATION_TYPE = #{wxProductQualificationType,jdbcType=TINYINT} ,WX_SECOND_CAT_ID = #{wxSecondCatId,jdbcType=VARCHAR} ,WX_SECOND_CAT_NAME = #{wxSecondCatName,jdbcType=VARCHAR} ,WX_FIRST_CAT_ID = #{wxFirstCatId,jdbcType=VARCHAR} ,WX_FIRST_CAT_NAME = #{wxFirstCatName,jdbcType=VARCHAR} ,STATUS = #{status,jdbcType=TINYINT} ,AUDIT_ID = #{auditId,jdbcType=VARCHAR} ,AUDIT_TIME = #{auditTime,jdbcType=TIMESTAMP} ,QUALIFICATION_URLS = #{qualificationUrls,jdbcType=VARCHAR} ,CREATE_BY = #{createBy,jdbcType=VARCHAR} ,CREATE_TIME = #{createTime,jdbcType=TIMESTAMP} ,UPDATE_BY = #{updateBy,jdbcType=VARCHAR} ,UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP} ,IS_DELETED = #{isDeleted,jdbcType=INTEGER} WHERE ID = #{id,jdbcType=BIGINT}
     * @param entity entity
     * @return Long
     */
    Long update(WechatCatogoryQualificationDO entity);
    /**
     * desc:delete:wechat_catogory_qualification.<br/>
     * descSql =  DELETE FROM wechat_catogory_qualification WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return Long
     */
    Long deleteByPrimary(Long id);
    /**
     * desc:get:wechat_catogory_qualification.<br/>
     * descSql =  SELECT * FROM wechat_catogory_qualification WHERE ID = #{id,jdbcType=BIGINT}
     * @param id id
     * @return WechatCatogoryQualificationDO
     */
    WechatCatogoryQualificationDO getByPrimary(Long id);

    WechatCatogoryQualificationDO getByAuditId(String auditId);

    WechatCatogoryQualificationDO getByWxCategoryId(String wxCategoryId);

    List<WechatCatogoryQualificationDO> list();

    int auditResult(@Param("auditId") String auditId, @Param("auditContent") String auditContent,@Param("status") int status);
}
