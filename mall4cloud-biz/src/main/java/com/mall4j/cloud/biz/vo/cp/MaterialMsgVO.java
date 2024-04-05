package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 素材消息表VO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
public class MaterialMsgVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("素材id")
    private Long matId;

    @ApiModelProperty("消息类型 text:文本,image:图片,video:视频,miniprogram:小程序 h5：H5页面 文件：file ")
    private String type;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息图片url")
    private String url;

    @ApiModelProperty("消息图片url")
    private String appId;

    @ApiModelProperty("小程序APPid")
    private String appTitle;

    @ApiModelProperty("小程序标题")
    private String appPage;

    @ApiModelProperty("小程序封面url")
    private String appPic;

}
