package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

/**
 * 视频号4.0 橱窗分店
 * @date 2023/3/8
 */
@Data
public class EcWindowBranchInfo {
    /**
     * 分店ID
     */
    private Long branch_id;

    /**
     * 分店名
     */
    private String branch_name;

    /**
     * 分店状态 0	营业中 1	停业
     */
    private Integer branch_status;
}
