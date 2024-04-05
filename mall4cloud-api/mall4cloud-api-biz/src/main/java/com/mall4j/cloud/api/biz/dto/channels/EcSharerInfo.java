package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcSharerInfo {
    //分享员openid
    private String sharer_openid;
    //分享员unionid
    private String sharer_unionid;
    //分享员类型，0：普通分享员，1：店铺分享员
    private Integer sharer_type;
    //分享场景
    private Integer share_scene;
}
