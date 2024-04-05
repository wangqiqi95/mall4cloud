package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author chaoge
 */
@Data
public class UserChangeServiceStoreDTO {

    @ApiModelProperty("变更后id")
    private Long afterId;

    @ApiModelProperty("变更后name")
    private String afterName;

    @ApiModelProperty("用户ID集合")
    private List<Long> userIds;

    @ApiModelProperty("变更前id")
    private Long beforeId;

    @ApiModelProperty("变更前name")
    private String beforeName;

    @ApiModelProperty("类型：0-服务门店变更 1-导购变更")
    private Integer type;

    @ApiModelProperty("操作来源：0-关店迁移 1-同步数据")
    private Integer source;

    @ApiModelProperty("变更人")
    private String creator;

}
