package com.mall4j.cloud.api.user.crm.request;

import lombok.Data;

@Data
public class QueryPointRecordRequest {

    //必传
    private String unionId;


    //积分变更类型SEND: 立即发放
    //DEDUCT: 扣减
    //不传查询全部
    private String recordTypes;
    //起始时间 格式:yyyy-MM-dd HH:mm:ss
    private String startTime;
    //结束时间 格式:yyyy-MM-dd HH:mm:ss
    private String endTime;
    //当前页 默认1
    private Integer page;
    //每页显示条数,默认20
    private Integer pageSize;

}
