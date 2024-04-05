package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

/**
 * 注册返回值
 * @author LGH
 */
@Data
public class RoomResponse {

    private Long roomId;

    private String qrcodeUrl;

    private String createdAt;

}
