package com.mall4j.cloud.api.biz.dto.channels.request;

import com.mall4j.cloud.api.biz.dto.channels.EcChangeOrderInfo;
import lombok.Data;

@Data
public class EcOrderUpdatePriceRequest {
    //订单id
    private String order_id;
    //是否修改运费
    private Boolean change_express;
    //修改后的运费价格（change_express=true时必填），以分为单位
    private Long express_fee;
    //改价列表
    private EcChangeOrderInfo change_order_infos;
}
