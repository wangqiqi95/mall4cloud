package com.mall4j.cloud.api.user.crm.response;

import lombok.Data;

import java.util.List;

@Data
public class QueryTagCategoryPageResponse {
    private Integer totalElement;
    private Integer pageNo;
    private Integer pageSize;
    //数据内容
    private List<QueryTagCategoryPage> content;
}
