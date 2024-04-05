package com.mall4j.cloud.api.biz.dto.cp.externalcontact;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.List;

@Data
public class CpChatAddMsgTemplateDTO {

    @ApiModelProperty(value = "对人：single，对群：group",required = true)
    private String chatType;

    @ApiModelProperty(value = "发送人企业微信ID（当类型为发送给客户群时必填）",required = true)
    private String sender;

    @ApiModelProperty(value = "文本内容")
    private Text text;

    @ApiModelProperty(value = "素材")
    private List<Attachment> attachments;

    @ApiModelProperty(value = "客户群id列表，仅在chat_type为group时有效",required = true)
    private List<String> chatIdList;

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

}
