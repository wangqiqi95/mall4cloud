package com.mall4j.cloud.biz.bo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生成任务调取业务对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateTaskExecuteInfoBO {
    private Long taskId;
}
