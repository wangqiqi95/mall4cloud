package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import lombok.Data;

import java.security.KeyStore;

@Data
public class ComplaintOrderDetail {

    private ComplaintOrder order;

    private FlowInfo flow_info;

}
