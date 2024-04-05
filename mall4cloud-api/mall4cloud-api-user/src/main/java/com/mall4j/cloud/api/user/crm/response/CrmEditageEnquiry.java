package com.mall4j.cloud.api.user.crm.response;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户数据查询
 */
@Data
public class CrmEditageEnquiry {

    @ApiModelProperty("询价单数据")
    private List<JSONObject> Enquiry;

    @ApiModelProperty("订单数据")
    private List<JSONObject> Order;

}
