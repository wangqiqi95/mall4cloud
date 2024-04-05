package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.cp.bean.WxCpUserDetail;

import java.io.Serializable;

/**
 * @Author: Administrator
 * @Description: 企业微信用户信息
 * @Date: 2022-01-18 20:56
 * @Version: 1.0
 */
@Data
public class WxCpUserInfoVO implements Serializable {
    @ApiModelProperty("成员UserID")
    private String userid;
    @ApiModelProperty("成员名称")
    private String name;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("成员所属部门id列表")
    private Integer[] departIds;
    @ApiModelProperty("职务信息")
    private String position;
    @ApiModelProperty("性别 0表示未定义，1表示男性，2表示女性")
    private String gender;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("员工个人二维码")
    private String qrCode;
    @ApiModelProperty("激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业")
    private Integer status;
    @ApiModelProperty("座机")
    private String telephone;
    @ApiModelProperty("头像url")
    private String avatar;
    @ApiModelProperty("头像缩略图url")
    private String thumbAvatar;
    @ApiModelProperty("sessionkey")
    private String sessionKey;

    /**
     * 网页授权登录：https://developer.work.weixin.qq.com/document/path/91022
     * scope为snsapi_privateinfo，且用户在应用可见范围之内时返回此参数。
     */
    private WxCpUserDetail wxCpUserDetail;
}
