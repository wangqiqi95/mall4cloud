package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * VO
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Data
public class WeixinShortlinkVO extends BaseVO{
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty("")
//    private Long id;
//
//    @ApiModelProperty("小程序appis")
//    private String appId;

    @ApiModelProperty("小程序页面路径")
    private String pagePath;

    @ApiModelProperty("短链key")
    private String shortKey;

	@ApiModelProperty("小程序短链")
	private String shortLink;

//    @ApiModelProperty("跳转长链接")
//    private String longUrl;

    @ApiModelProperty("域名")
    private String doMain;

    @ApiModelProperty("携带参数")
    private String scen;
}
