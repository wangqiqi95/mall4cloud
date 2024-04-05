package com.mall4j.cloud.biz.dto.cp.wx;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
public class WxCpJoinWayResult extends WxCpBaseResp {
    private static final long serialVersionUID = -2612867517869192015L;

    @SerializedName("config_id")
    private String configId;


    public static WxCpJoinWayResult fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpJoinWayResult.class);
    }
}
