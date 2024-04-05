package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信触点门店二维码表VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
@Data
public class WeixinQrcodeTentacleStoreVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("触点id")
    private String tentacleId;

    @ApiModelProperty("门店id")
    private String storeId;
    @ApiModelProperty("门店编码")
    private String storeCode;
	@ApiModelProperty("门店名称")
	private String storeName;

    @ApiModelProperty("跳转路径")
    private String path;

    private String scene;

    @ApiModelProperty("二维码尺寸")
    private Integer codeWidth;

    @ApiModelProperty("二维码路径")
    private String qrcodePath;

    @ApiModelProperty("状态： 0可用 1不可用")
    private Integer status;

    @ApiModelProperty("打开次数")
    private Integer openNumber;

    @ApiModelProperty("打开人数")
    private Integer openPeople;

}
