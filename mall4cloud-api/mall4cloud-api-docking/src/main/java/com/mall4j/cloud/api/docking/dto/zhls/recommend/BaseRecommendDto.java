package com.mall4j.cloud.api.docking.dto.zhls.recommend;

import lombok.Data;

/**
 * @Description
 * @Author axin
 * @Date 2023-03-09 14:54
 **/
@Data
public class BaseRecommendDto<T> {
    private Integer code;
    private String message;
    private String sequence_id;
    /**
     * 总记录数
     */
    private Long hits;

    /**
     * 是否有更多
     */
    private Boolean hasMore;
    private String requestId;
    private String strategy;
    private T data;

    public boolean isSuccess(){
        return code != null && code.equals(0);
    }
}
