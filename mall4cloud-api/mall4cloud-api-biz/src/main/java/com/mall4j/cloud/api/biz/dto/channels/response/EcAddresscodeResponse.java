package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcAddrMsg;
import lombok.Data;

import java.util.List;

@Data
public class EcAddresscodeResponse extends EcBaseResponse {

    //本行政编码地址信息
    private EcAddrMsg addrs_msg;
    //下一级所有地址信息
    private List<EcAddrMsg> next_level_addrs;
}
