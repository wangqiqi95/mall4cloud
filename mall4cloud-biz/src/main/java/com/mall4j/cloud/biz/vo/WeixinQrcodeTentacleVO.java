package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信触点二维码表VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
@Data
public class WeixinQrcodeTentacleVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("触点链接")
    private String tentaclePath;

    @ApiModelProperty("接收邮箱")
    private String recevieEmail;

    @ApiModelProperty("打开次数")
    private Integer openNumber;

    @ApiModelProperty("打开人数")
    private Integer openPeople;

    @ApiModelProperty("状态： 0可用 1不可用")
    private Integer status;

}
