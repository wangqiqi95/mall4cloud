package com.mall4j.cloud.api.openapi.skq_crm.dto;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SubscriptMessageRequest  implements Serializable, IDataCheck{

    @ApiModelProperty(value = "消息id")
    private String requestId;

    @ApiModelProperty(value = "会员vipcode")
    private String vipcode;

    @ApiModelProperty(value = "会员手机号码")
    private String phone;

    @ApiModelProperty(value = "业务场景1. 新会员入会礼券到账通知\n" +
            "2. 新会员入会礼券使用提醒\n" +
            "3. 会员完善资料提醒 \n" +
            "4. 会员升级送券到账通知 \n" +
            "5. 会员保级通知\n" +
            "6. 会员降级通知\n" +
            "7. 会员生日礼券到账通知\n" +
            "8. 优惠券到账提醒\n" +
            "9. 优惠券到期提醒   (到期前7天)\n" +
            "10. 兑礼券到期提醒(到期前3天) ")
    private Integer scene;

    @ApiModelProperty(value = "会员注册时间 yyyy-MM-dd HH:mm:ss")
    private String regTime;

    @ApiModelProperty(value = "会员当前等级")
    private Integer currentLevel;
    @ApiModelProperty(value = "会员当前等级名称")
    private String currentLevelName;


    @ApiModelProperty(value = "原会员等级")
    private Integer beforLevel;
    @ApiModelProperty(value = "原会员等级名称")
    private String beforLevelName;

    @ApiModelProperty(value = "变更前等级")
    private Integer beforChangeLevel;
    @ApiModelProperty(value = "变更前等级名称")
    private String beforChangeLevelName;
    @ApiModelProperty(value = "变更前等级失效日期 yyyy-MM-dd HH:mm:ss")
    private String beforChangeLevelExpireDate;


    @ApiModelProperty(value = "优惠券名称 ")
    private String couponName;
    @ApiModelProperty(value = "优惠券失效时间 yyyy-MM-dd HH:mm:ss")
    private String couponExpireDate;
    @ApiModelProperty(value = "优惠券图标类型")
    private Integer couponLogoType;

    @ApiModelProperty(value = "提示文案 注意：订阅消息，字符类型最大长度是32。")
    private String tips;




    @Override
    public CrmResponse check() {
        return null;
    }
}
