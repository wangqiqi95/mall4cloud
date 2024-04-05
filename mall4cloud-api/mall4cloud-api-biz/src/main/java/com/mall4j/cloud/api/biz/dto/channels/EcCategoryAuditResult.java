package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcCategoryAuditResult {
    //审核状态, 1：审核中，3：审核成功，2：审核拒绝，12：主动取消申请单
    private Integer status;
    //如果审核拒绝，返回拒绝原因
    private String reject_reason;
}
