package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateScoreDTO {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "变动积分", required = true)
    private Integer point_value;

    @ApiModelProperty(value = "来源", required = true)
    private String source;

    @ApiModelProperty(value = "业务id")
    private Long bizId;

    @ApiModelProperty(value = "出入类型 0=支出 1=收入", required = true)
    private Integer ioType;

    @ApiModelProperty(value = "店铺id")
    private String store_id;

    @ApiModelProperty(value = "积分渠道,wechat:微信\n" + "offline_act:线下活动\n" + "self_store:自营门店\n" + "dist_store:经销商门店\n" + "web:官网\n" + "smart__store:智慧门店\n"
            + "tmall:会员通\n" + "crm_backstage:CRM后台\n" + "ccms:主动营销\n" + "other:其他\n", required = true)
    private String point_channel;

    @ApiModelProperty(value = "积分类型,SKX_XFJF:消费积分\n" + "SKX_HDHD:互动活动\n" + "SKX_XWJF:行为积分\n" + "SKX_JLJF:奖励积分\n" + "SKX_RGTZ:人工调整\n" + "SKX_JFDH:积分兑换\n"
            + "SYSTEM_OVERDUE:积分过期\n" + "SKX_JFDX:积分抵现\n" + "SKX_ZDYX:主动营销\n", required = true)
    private String point_type;

    @ApiModelProperty(value = "订单号")
    private String order_id;

    @ApiModelProperty(value = "备注")
    private String remark;
}
