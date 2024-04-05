package com.mall4j.cloud.biz.dto.channels.sharer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-23 15:15
 **/
@Data
public class SharerPageListRespDto {
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "员工id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long staffId;

    @ApiModelProperty(value = "员工工号")
    private String staffNo;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value ="分享员类型 0普通分享员 1企业分享员")
    private Integer sharerType;

    @ApiModelProperty(value ="分享员类型 0普通分享员 1企业分享员")
    private String sharerTypeName;

    @ApiModelProperty(value = "绑定时间")
    private Date bindTime;

    @ApiModelProperty(value = "解绑时间")
    private Date unbindTime;

    @ApiModelProperty(value = "二维码创建时间")
    private Date qrcodeImgCreateTime;

    @ApiModelProperty(value = "二维码失效时间")
    private Date qrcodeImgExpireTime;

    @ApiModelProperty(value = "绑定状态 0 初始化 1待绑定：有邀请码 2失效：邀请码失效 3成功：有绑定时间 4已解绑")
    private Integer status;

    @ApiModelProperty(value = "绑定状态 1待绑定：有邀请码 2失效：邀请码失效 3成功：有绑定时间 4已解绑")
    private String statusName;

    @ApiModelProperty(value = "异常信息")
    private String errorMsg;

    /**
     * 绑定状态 0初始化 1待绑定：有邀请码 2失效：邀请码失效 3成功：有绑定时间 4已解绑
     */
    private Integer bindStatus;
}
