package com.mall4j.cloud.api.user.crm.request;

import lombok.Data;

@Data
public class ChangePointRequest {

    //微信unionId 必填参数
    private String unionId;
    //变更积分值
    private Integer point;
    //积分变更类型
    //SEND: 立即发放,DEDUCT: 扣减
    private String changeType;
    //备注
    private String description;
    //渠道
    private String channelType;
    //会员类型
    private String memberType;
    //请求id，幂等控制
    private String businessToken;


    //下面为非必传接口
    //积分生效时间
    //格式: yyyy-MM-dd HH:mm:ss，为空时立即生效
    private String effectTime;
    //积分过期时间
    //格式: yyyy-MM-dd HH:mm:ss 不填时按照客户约定失效时间
    private String expiredTime;


}
