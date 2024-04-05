package com.mall4j.cloud.api.docking.skq_sqb.dto.request.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *  请求头部信息
 * @author Peter_Tan
 * @date
 **/
@Data
public class SQBHead {

    /**
     * 签名类型
     */
    private String sign_type;

    /**
     * appid
     */
    private String appid;

    /**
     * 请求时间
     */
    private String request_time;

    /**
     * 版本
     */
    private String version;
    
    /**
     * 备注
     */
    private String reserve;

}
