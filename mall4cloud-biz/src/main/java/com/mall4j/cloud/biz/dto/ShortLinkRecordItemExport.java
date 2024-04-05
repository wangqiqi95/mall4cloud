package com.mall4j.cloud.biz.dto;

import lombok.Data;

/**
 * @Description 短链记录明细导出
 * @Author tan
 * @Date 2022-12-29 10:54
 **/
@Data
public class ShortLinkRecordItemExport {
    public final static String SHORT_LINK_RECORD_ITEM_DETAIL_FILE_NAME = "短链记录明细";

    /**
     * 入参条件
     */
    private WeixinShortlinkRecordItemPageDTO weixinShortlinkRecordItemPageDTO;

    /**
     * 下载中心ID
     */
    private Long downloadCenterId;
}
