package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * VO
 *
 * @author lt
 * @date 2022-01-17
 */
@Data
@ApiModel("视频号物流信息")
public class LiveLogisticsVO extends BaseVO{

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("物流公司编号")
    private String logisticsCode;

    @ApiModelProperty("物流公司名称")
    private String logisticsName;

    @ApiModelProperty("物流公司电话")
    private String phone;

    @ApiModelProperty("合作物流公司编号")
    private String coLogisticsCode;

    @ApiModelProperty("合作物流公司名称")
    private String coLogisticsName;

    @ApiModelProperty("合作物流公司电话")
    private String coLogisticsPhone;
    
    @ApiModelProperty("视频号4.0物流公司编码")
    private String deliveryCompanyId;
    
    @ApiModelProperty("视频号4.0快递公司名称")
    private String deliveryCompanyName;

}
