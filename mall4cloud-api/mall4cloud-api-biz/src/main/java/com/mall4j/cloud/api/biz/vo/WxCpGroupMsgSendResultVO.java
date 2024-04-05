package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Peter_Tan
 * @Description: 调用企业微信获取企业群发成员执行结果
 * @date : 2023-03-20
 * @Version: 1.0
 */
@Data
public class WxCpGroupMsgSendResultVO implements Serializable {

    @ApiModelProperty("返回码")
    protected Long errcode;

    @ApiModelProperty("对返回码的文本描述内容")
    protected String errmsg;

    @ApiModelProperty("分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空")
    private String nextCursor;

    @ApiModelProperty("群成员发送结果列表")
    private List<WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> sendList;

    @Data
    public static class ExternalContactGroupMsgTaskInfo implements Serializable {
        private static final long serialVersionUID = 1500416806087532531L;
        @ApiModelProperty("外部联系人userid")
        private String externalUserId;

        @ApiModelProperty("外部客户群id")
        private String chatId;

        @ApiModelProperty("企业服务人员的userid")
        private String userId;

        @ApiModelProperty("发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
        private Integer status;

        @ApiModelProperty("发送时间，发送状态为1时返回")
        private Long sendTime;
    }
}
