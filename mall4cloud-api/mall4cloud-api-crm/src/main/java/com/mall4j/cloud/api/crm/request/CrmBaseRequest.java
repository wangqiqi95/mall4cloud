package com.mall4j.cloud.api.crm.request;


import lombok.Data;

@Data
public class CrmBaseRequest {

    /**
     * 业务代码:如scrm【必填】
     */
    private String bizCode;

    /**
     * 请求渠道 POS;WECHAT:微信【必填】
     */
    private String requestChannel;

    /**
     * 请求系统 POS;WECHAT_MALL:微信商城;MEMBER_CENTER:会员中心;POINTS_MALL:积分商城【必填】
     */
    private String requestSystem;

    /**
     * 事务ID【必填】
     */
    private String transactionId;


    /**
     * 入参【必填】
     */
    private String identify;

    public void init(String traceId){
        this.setBizCode("scrm");
        this.setRequestChannel("scrm");
        this.setRequestSystem("scrm");
        this.setTransactionId(traceId);
    }

}
