package com.mall4j.cloud.api.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信小程序订阅模版消息VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:44
 */
@Data
public class WeixinMaSubscriptTmessageVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("模版业务类型id")
    private String templateTypeId;

    @ApiModelProperty("100.会员生日到期提醒 200.优惠券到期提醒 201.优惠券到账提醒 300.积分变更提醒 " +
            "301.等级到期提醒  400.订单发货提醒  401.订单签收提醒 500.退单审核结果提醒 501.退单完成提醒 " +
            "600.活动上新提醒 601.活动开始提醒 602.活动结束提醒")
    private Integer sendType;

    @ApiModelProperty("系统模板名称")
    private String title;

    @ApiModelProperty("小程序模版code")
    private String templateCode;

    @ApiModelProperty("模板标题")
    private String templateTitle;

    @ApiModelProperty("模板示例")
    private String example;

    @ApiModelProperty("小程序appid")
    private String appId;

    @ApiModelProperty("要跳转的页面地址，可以拼接参数。")
    private String page;

    @ApiModelProperty("0禁用 1启用")
    private Integer status;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

    @ApiModelProperty("模版参数列表")
    private List<WeixinMaSubscriptTmessageValueVO> tmessageValues;

    @ApiModelProperty("订阅消息接收者")
    private List<WeixinMaSubscriptUserRecordVO> userRecords;

}
