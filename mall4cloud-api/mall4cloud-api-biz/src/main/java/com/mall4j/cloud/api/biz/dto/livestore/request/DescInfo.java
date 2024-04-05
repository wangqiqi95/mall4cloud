
package com.mall4j.cloud.api.biz.dto.livestore.request;

import lombok.Data;

import java.util.List;

@Data
public class DescInfo {

    /**
     * 商品详情图文
     */
    private String desc;
    /**
     * 商品详情图片
     */
    private List<String> imgs;


}
