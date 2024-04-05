package com.mall4j.cloud.biz.dto.cp.wx.externalcontact;

import lombok.Data;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplate;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.List;

@Data
public class ExtendWxCpMsgTemplate extends WxCpMsgTemplate {

    private boolean allow_select;


    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
