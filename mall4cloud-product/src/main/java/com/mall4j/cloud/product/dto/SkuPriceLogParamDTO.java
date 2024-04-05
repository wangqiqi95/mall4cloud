package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
@Data
public class SkuPriceLogParamDTO extends PageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索关键字")
    private String searchKey;

    @ApiModelProperty("门店Id")
    private Long storeId;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("日志类型：0-同步商品基础数据 1-同步吊牌价 2-同步保护价 3-同步pos价 4-同步库存 5-商品批量改价 6-批量设置渠道价 7-批量取消渠道价 8-同步吊牌价重算门店pos价")
    private Integer logType;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


}
