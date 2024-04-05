package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import java.io.Serializable;

/**
 * 类描述：获取商品详细信息请求参数
 */
public class GoodsReqDto extends CommonReqDto implements Serializable {

    private static final long serialVersionUID = 2892380897775652782L;
    private String itemid;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    @Override
    public String toString() {
        return super.toString() + ",GoodsReqDto{" + "itemid='" + itemid + '\'' + '}';
    }
}
