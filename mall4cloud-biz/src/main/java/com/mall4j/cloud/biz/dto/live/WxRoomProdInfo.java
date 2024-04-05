package com.mall4j.cloud.biz.dto.live;

import lombok.Data;

import java.util.List;

@Data
public class WxRoomProdInfo extends WxInterfaceInfo{

    private List<Long> ids;

    private Long roomId;

    @Override
    public String getRequestUrl() {
        return "/wxaapi/broadcast/room/addgoods?access_token=";
    }

}
