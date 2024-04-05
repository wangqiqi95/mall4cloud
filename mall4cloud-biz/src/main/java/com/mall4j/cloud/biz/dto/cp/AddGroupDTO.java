package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-02-17 14:39
 * @Version: 1.0
 */
@Data
public class AddGroupDTO implements Serializable {
    @ApiModelProperty("群活码id")
    private Long id;
    @ApiModelProperty("群id列表")
    private List<String> groupIds;
}
