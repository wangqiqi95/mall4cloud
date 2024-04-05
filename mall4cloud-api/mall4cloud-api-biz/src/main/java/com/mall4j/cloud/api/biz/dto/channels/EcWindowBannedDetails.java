package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

/**
 * 视频号4.0 橱窗商品封禁详情
 * @date 2023/3/8
 */
@Data
public class EcWindowBannedDetails {
    /**
     * 禁售原因 0	三级类目在橱窗禁售 或 商品在来源处被禁售 1	商品属于可申请售卖的类目，但商家未完成申请 2 商品所属分店未处于营业状态
     */
    private Integer reason;

    /**
     * 未申请售卖对应商品类目时带有，需要申请的类目ID
     */
    private String need_apply_category_id;

    /**
     * 未申请售卖对应商品类目时带有，需要申请的类目名
     */
    private String need_apply_category_name;
}
