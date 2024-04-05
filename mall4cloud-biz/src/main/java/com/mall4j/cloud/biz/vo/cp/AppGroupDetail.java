package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 移动端-引流页面详情
 * ：群活码、自动拉群、标签建群
 */
@Data
public class AppGroupDetail {

    @ApiModelProperty("源id")
    private String sourceId;

    //CodeChannelEnum
    @ApiModelProperty("源信息")
    private Integer sourceFrom;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("群聊信息")
    private List<AppGroupRefInfo> infoList;

    @ApiModelProperty("渠道二维码")
    private String qrCode;

    @ApiModelProperty("活码名称")
    private String codeName;

}
