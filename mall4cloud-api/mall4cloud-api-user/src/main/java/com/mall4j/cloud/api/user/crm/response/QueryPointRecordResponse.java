package com.mall4j.cloud.api.user.crm.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPointRecordResponse {
    //变更积分
    private Integer ponint;
    //历史总积分
    private Integer totalPoint;
    //积分变更类型 SEND: 立即发放, DELAY_SEND: 延迟发放, EXPIRE: 过期, FREEZE: 冻结, UNFREEZE: 取消冻结, DEDUCT: 扣减, ABOLISH: 作废, TIMER: 定时, RECALCULATE: 废弃重算, SPECIAL_DEDUCT: 特殊扣除, SPECIAL_FREEZE: 特殊冻结, SPECIAL_UNFREEZE: 特殊解冻, SPECIAL_ABOLISH: 特殊废弃, MANUAL_ABOLISH: 手动废弃,OPEN_FREEZE: 接口冻结, OPEN_UNFREEZE: 接口解冻
    private String changeType;
    //备注
    private String description;
    //积分生效时间
    private String effectiveTime;
    //积分失效时间
    private String expiredTime;
    //变更时间
    private String changeTime;
    //渠道类型
    private String channel;


}
