package com.mall4j.cloud.biz.dto.channels.sharer;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-23 15:15
 **/
@Data
public class SharerPageListExportRespDto {
    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "员工id")
    private Long staffId;

    @ExcelProperty(value = "员工工号")
    private String staffNo;

    @ExcelProperty(value = "员工姓名")
    private String name;

    @ExcelIgnore
    @ApiModelProperty(value ="分享员类型 0普通分享员 1企业分享员")
    private String sharerType;

    @ExcelProperty(value = "分享员类型")
    @ApiModelProperty(value ="分享员类型 0普通分享员 1企业分享员")
    private String sharerTypeName;

    @ExcelProperty(value = "绑定时间")
    private Date bindTime;

    @ExcelIgnore
    @ApiModelProperty(value = "解绑时间")
    private Date unbindTime;

    @ExcelIgnore
    @ApiModelProperty(value = "二维码创建时间")
    private Date qrcodeImgCreateTime;

    @ExcelIgnore
    @ApiModelProperty(value = "二维码失效时间")
    private Date qrcodeImgExpireTime;

    @ExcelIgnore
    @ApiModelProperty(name = "绑定状态 1待绑定：有邀请码 2失效：邀请码失效 3成功：有绑定时间 4已解绑")
    private Integer status;

    @ExcelProperty(value = "绑定状态")
    private String statusName;

    @ExcelProperty(value = "失败原因")
    private String errorMsg;
}
