package com.mall4j.cloud.api.docking.skq_sqb.dto.request.common;

import lombok.Data;

/**
 * @Description 收钱吧轻POS请求信息
 * @author Peter_Tan
 * @date
 **/
@Data
public class SQBReq {

    /**
     * 请求体
     */
    private CommonRequest request;

    /**
     * 签名、验签
     */
    private String signature;

}
