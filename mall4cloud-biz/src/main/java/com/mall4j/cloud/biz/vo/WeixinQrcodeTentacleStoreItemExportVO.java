package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 查询触点门店列表详情参数
 *
 * @author tan
 * @date 2022-03-09 16:05:09
 */
@Data
public class WeixinQrcodeTentacleStoreItemExportVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("触点门店id")
    private String tentacleStoreId;

    @ApiModelProperty("门店id")
    private String storeId;

    @ApiModelProperty("门店编码")
    private String storeCode;

	@ApiModelProperty("门店名称")
	private String storeName;

    @ApiModelProperty("用户uniId")
    private String uniId;

    @ApiModelProperty("会员ID/会员编号")
    private String vipCode;

    @ApiModelProperty("会员名称/用户昵称")
    private String nickName;

    @ApiModelProperty("查看时间")
    private Date checkTime;

}
