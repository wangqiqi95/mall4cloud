package com.mall4j.cloud.api.biz.dto.cp.externalcontact;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.List;

@Data
public class ExtendWxCpMsgTemplateDTO {

    @ApiModelProperty(value = "对人：single，对群：group",required = true)
    private String chat_type;
    @ApiModelProperty(value = "群发客户ID集合（对人时必填）")
    private List<String> external_userid;
    @ApiModelProperty(value = "发送人企业微信ID（当类型为发送给客户群时必填）")
    private String sender;
    @ApiModelProperty(value = "文本内容")
    private Text text;
    @ApiModelProperty(value = "素材")
    private List<Attachment> attachments;

    @ApiModelProperty(value = "是否允许成员在待发送客户列表中重新进行选择，默认为false，仅支持客户群发场景")
    private boolean allow_select;

    @ApiModelProperty(value = "客户群id列表，仅在chat_type为group时有效")
    private List<String> chat_id_list;

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

}
