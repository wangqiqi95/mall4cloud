package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 微信图文模板表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
@Data
public class WeixinNewsTemplateContentVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("模板id")
    private String id;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板类型：1单图文 2多图文")
    private String templateType;

    @ApiModelProperty("模板来源： 0微信图文素材 1自动回复素材")
    private Integer fromType;

    @ApiModelProperty("图文素材媒体id")
    private String mediaId;

    @ApiModelProperty("公众号原始id")
    private String appId;

    @ApiModelProperty("模板素材内容")
    private List<WeixinNewsItemVO> newsItemList;

}
