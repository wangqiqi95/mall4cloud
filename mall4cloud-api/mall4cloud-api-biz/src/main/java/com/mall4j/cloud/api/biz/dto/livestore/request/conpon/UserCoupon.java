package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class UserCoupon {

    private String out_user_coupon_id;
    private String out_coupon_id;
    private Integer status;
    private String openid;

    private ExtInfo ext_info;

    private Long create_time;
    private Long update_time;
    private Long end_time;
    private Long start_time;

    private Long recv_time;

}
