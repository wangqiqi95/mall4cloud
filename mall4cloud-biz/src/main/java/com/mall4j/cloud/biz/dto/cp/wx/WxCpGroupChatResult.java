package com.mall4j.cloud.biz.dto.cp.wx;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 「联系我」方式 对象
 *
 * @author element
 */
@Data
@NoArgsConstructor
public class WxCpGroupChatResult implements Serializable {
    private static final long serialVersionUID = -8697184659526210472L;

    @SerializedName("join_way")
    private JoinWay joinWay;

    @Getter
    @Setter
    public static class JoinWay implements Serializable {
        private static final long serialVersionUID = -8697184659526210472L;

        /**
         * 联系方式的配置id
         */
        @SerializedName("config_id")
        private String configId;

        private Integer type;
        /**
         * <pre>
         * 必填
         * 场景，1-在小程序中联系，2-通过二维码联系
         * </pre>
         */
        private SCENE scene;

        private String remark;

        @SerializedName("auto_create_room")
        private String autoCreateRoom;

        @SerializedName("room_base_name")
        private String roomBaseName;

        @SerializedName("room_base_id")
        private String roomBaseId;

        @SerializedName("chat_id_list")
        private List<String> chatIdList;

        private String state;

        @SerializedName("qr_code")
        private String qrCode;
    }


    public static WxCpGroupChatResult fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpGroupChatResult.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

    public enum SCENE {

        /**
         * 在小程序中联系
         */
        @SerializedName("1")
        MINIPROGRAM,

        /**
         * 通过二维码联系
         */
        @SerializedName("2")
        QRCODE

    }

}

