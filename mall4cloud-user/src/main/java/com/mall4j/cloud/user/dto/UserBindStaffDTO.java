package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserBindStaffDTO {

    @ApiModelProperty("会员集合")
    private List<UserBindStaffUserDataDTO> userDataList;
    @NotNull(message = "bindType不能为空")
    @ApiModelProperty("分配类型 1-指定分配 2-随机分配")
    private Integer bindType;
    @ApiModelProperty("分配员工id")
    private Long staffId;
    @ApiModelProperty("分配员工名称")
    private String staffName;




}
