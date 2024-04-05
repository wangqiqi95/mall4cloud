package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffGetUserDetailByMaterialVO {

    @ApiModelProperty("会员详情")
    private UserApiVO userDetail;

    @ApiModelProperty("ID")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("姓名")
    private String customerName;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户积分")
    private Long score;

    @ApiModelProperty(value = "crm会员等级，1---新奇，2---好奇，3---炫奇，4---珍奇")
    private String currentGradeId;

    @ApiModelProperty("注册门店ID")
    private Long storeId;

    @ApiModelProperty("注册门店名称")
    private String storeName;

    @ApiModelProperty("注册门店编码")
    private String storeCode;

    @ApiModelProperty("服务门店ID")
    private Long serviceStoreId;

    @ApiModelProperty("服务门店名称")
    private String serviceStoreName;

    @ApiModelProperty("服务门店编码")
    private String serviceStoreCode;

}
