package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class ReceiveInfo {
    //领取开始时间
    private Long start_time;
    //领取结束时间
    private Long end_time;
    //限领张数，由小程序保证限领
    private Integer limit_num_one_person;
    //总发放量
    private Integer total_num;
}
