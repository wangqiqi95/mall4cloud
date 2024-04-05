package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcLimitedInfo {
    //限购周期类型，0无限购（默认），1按自然日限购，2按自然周限购，3按自然月限购
    private Integer period_type;
    //	限购数量
    private Integer limited_buy_num;
}
