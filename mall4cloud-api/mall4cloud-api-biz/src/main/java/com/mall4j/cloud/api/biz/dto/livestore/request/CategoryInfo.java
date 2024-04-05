
package com.mall4j.cloud.api.biz.dto.livestore.request;

import lombok.Data;

import java.util.List;

@Data
public class CategoryInfo {

    private List<String> certificate;
    private Long level1;
    private Long level2;
    private Long level3;

}
