package com.mall4j.cloud.biz.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信触点门店二维码表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
@Data
public class WeixinQrcodeTentacleStoreExportDTO extends PageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("触点门店id")
    private List<String> tentacleStoreIds;

}
