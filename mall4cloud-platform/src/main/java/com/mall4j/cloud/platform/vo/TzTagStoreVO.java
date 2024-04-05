package com.mall4j.cloud.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 标签关联门店VO
 *
 * @author gmq
 * @date 2022-09-13 12:01:58
 */
@Data
public class TzTagStoreVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("标签id")
    private Long tagId;

    @ApiModelProperty("门店id")
    private Long storeId;

	@ApiModelProperty("门店名称")
	private String storeName;

    @ApiModelProperty("门店编码")
    private String storeCode;

}
