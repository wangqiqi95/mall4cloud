package com.mall4j.cloud.biz.dto.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AddQrCodeDTO {
    @ApiModelProperty("群id")
    @NotBlank(message = "群id不能为空！")
    private String id;
    @ApiModelProperty("二维码路径")
    @NotBlank(message = "二维码路径不能为空！")
    private String qrCode;
    @ApiModelProperty("二维码有效期")
    @NotNull(message = "二维码有效期不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
}

