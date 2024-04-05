package com.mall4j.cloud.api.docking.skq_sqb.dto.request.common;

import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBBody;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBHead;
import lombok.Data;

/**
 * @author ty
 * @ClassName CommonRequest
 * @description
 * @date 2023/5/6 10:18
 */
@Data
public class CommonRequest {

    /**
     * 头部信息
     */
    private SQBHead head;

    /**
     * 请求参数主体
     */
    private SQBBody body;

}
