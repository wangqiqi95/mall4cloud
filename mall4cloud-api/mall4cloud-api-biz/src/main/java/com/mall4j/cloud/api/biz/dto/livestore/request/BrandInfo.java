
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BrandInfo {

    /**
     * 认证审核类型 RegisterType
     */
    @JsonProperty("brand_audit_type")
    private Integer brandAuditType;
    /**
     * 选择品牌经营类型 BrandManagementType
     */
    @JsonProperty("brand_management_type")
    private Integer brandManagementType;
    /**
     * 商标/品牌词
     */
    @JsonProperty("brand_wording")
    private String brandWording;
    /**
     * 商品产地是否进口 CommodityOriginType
     */
    @JsonProperty("commodity_origin_type")
    private Integer commodityOriginType;
    /**
     * 中华人民共和国海关进口货物报关单，图片url/media_id
     */
    @JsonProperty("imported_goods_form")
    private List<String> importedGoodsForm;
    /**
     * 销售授权书（如商持人为自然人，还需提供有其签名的身份证正反面扫描件)，图片url/media_id
     */
    @JsonProperty("sale_authorization")
    private List<String> saleAuthorization;
    /**
     * 商标申请人姓名
     */
    @JsonProperty("trademark_applicant")
    private String trademarkApplicant;
    /**
     * 商标申请时间, yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty("trademark_application_time")
    private String trademarkApplicationTime;
    /**
     * 商标有效期，yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty("trademark_authorization_period")
    private String trademarkAuthorizationPeriod;
    /**
     * 商标变更证明，图片url/media_id
     */
    @JsonProperty("trademark_change_certificate")
    private List<String> trademarkChangeCertificate;
    /**
     * 商标注册人姓名
     */
    @JsonProperty("trademark_registrant")
    private String trademarkRegistrant;
    /**
     * 商标注册号/申请号
     */
    @JsonProperty("trademark_registrant_nu")
    private String trademarkRegistrantNu;
    /**
     * 商标注册申请受理通知书，图片url/media_id
     */
    @JsonProperty("trademark_registration_application")
    private List<String> trademarkRegistrationApplication;
    /**
     * 商标注册证书，图片url/media_id
     */
    @JsonProperty("trademark_registration_certificate")
    private List<String> trademarkRegistrationCertificate;
    /**
     * 商标分类 TrademarkType
     */
    @JsonProperty("trademark_type")
    private String trademarkType;

}
