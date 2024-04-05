package com.mall4j.cloud.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class OrderRefundOfflineCertificates {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long refundId;

    private String refundNumber;
    private Long orderId;
    private String orderNumber;
    private Long aftersaleId;
    private String certificates;
    private String refundDesc;
    private Date createTime;
    private Date updateTime;

}
