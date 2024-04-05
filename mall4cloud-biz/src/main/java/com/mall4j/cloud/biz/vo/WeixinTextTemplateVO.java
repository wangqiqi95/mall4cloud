package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信文本模板表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:52:50
 */
@Data
public class WeixinTextTemplateVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

//    @ApiModelProperty("模板名称")
//    private String templateName;

    @ApiModelProperty("模板内容")
    private String templateContent;

    @ApiModelProperty("超文本链接信息")
    private String textHerfs;

//    @ApiModelProperty("公众号原始id")
//    private String appId;
}
