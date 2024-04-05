package com.mall4j.cloud.biz.dto;

import lombok.Data;

/**
 * @Description 触点门店记录导出
 * @Author tan
 * @Date 2023-01-06 13:26
 **/
@Data
public class QrcodeTentacleStoreExport {
    public final static String QRCODE_TENTACLE_STORE_FILE_NAME = "微信门店触点记录";

    /**
     * 入参条件
     */
    private WeixinQrcodeTentacleStoreExportDTO weixinQrcodeTentacleStoreExportDTO;

    /**
     * 下载中心ID
     */
    private Long downloadCenterId;
}
