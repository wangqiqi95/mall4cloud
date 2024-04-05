package com.mall4j.cloud.api.user.crm.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户数据查询
 */
@Data
public class CrmEditageUser {

    @ApiModelProperty("客户编码")
    private String userId;

    @ApiModelProperty("邮箱")
    private String email_id;

    @ApiModelProperty("partner_id")
    private String partner_id;

    @ApiModelProperty("network_id")
    private String network_id;

    @ApiModelProperty("client_code")
    private String client_code;

    @ApiModelProperty("类型")
    private String client_type;

    @ApiModelProperty("角色")
    private String roles;

    @ApiModelProperty("用户是否被注销")
    private boolean active;

    @ApiModelProperty("创建时间")
    private String created_date;

    @ApiModelProperty("用户是否被注销  active")
    private String status;

    @ApiModelProperty("时区")
    private String timezone;

    @ApiModelProperty("客户端代码id")
    private String client_code_id;

    @ApiModelProperty("语言")
    private String languag;

    @ApiModelProperty("用户是否被注销")
    private String type;

    @ApiModelProperty("Eos启用日期")
    private String eos_enabled;

    @ApiModelProperty("手机号")
    private String mobile_number;

    @ApiModelProperty("客户生日")
    private String field_client_profile_dob;

    @ApiModelProperty("客户所属组织")
    private String field_client_profile_org;

    @ApiModelProperty("支付偏好部门")
    private String field_pay_pref_department;

    @ApiModelProperty("客户组类别")
    private String field_client_group_category;

    @ApiModelProperty("支付偏好名字")
    private String field_cp_pay_pref_firstname;

    @ApiModelProperty("支付偏好机构")
    private String field_pay_pref_organization;

    @ApiModelProperty("客户个人网站")
    private String field_client_profile_website;

    @ApiModelProperty("客户组优惠码")
    private String field_client_group_coupon_code;

    @ApiModelProperty("备用电子邮件")
    private String field_client_profile_alt_email;

    @ApiModelProperty("客户职位")
    private String field_client_profile_job_title;

    @ApiModelProperty("部门")
    private String field_client_profile_department;

    @ApiModelProperty("电子邮件标志")
    private String field_client_profile_email_flag;

}
