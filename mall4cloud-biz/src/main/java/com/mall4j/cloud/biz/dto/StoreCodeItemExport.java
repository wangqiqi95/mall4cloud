package com.mall4j.cloud.biz.dto;

import lombok.Data;

/**
 * @Description 触点记录明细导出
 * @Author tan
 * @Date 2022-12-29 10:26
 **/
@Data
public class StoreCodeItemExport {
    public final static String STORE_CODE_ITEM_DETAIL_FILE_NAME = "触点记录明细";

    /**
     * 入参条件
     */
    private WeixinQrcodeTentacleStoreItemDTO weixinQrcodeTentacleStoreItemDTO;

    /**
     * 下载中心ID
     */
    private Long downloadCenterId;
}
