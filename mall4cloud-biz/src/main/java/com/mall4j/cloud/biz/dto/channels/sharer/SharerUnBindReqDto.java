package com.mall4j.cloud.biz.dto.channels.sharer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-23 13:50
 **/
@Data
public class SharerUnBindReqDto {

    @ApiModelProperty(value = "id")
    private Long id;
}
