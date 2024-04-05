package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class ValidInfo {

    private Integer valid_type;
    private Integer valid_day_num;
    private Integer valid_second;
    private Long start_time;
    private Long end_time;

}
