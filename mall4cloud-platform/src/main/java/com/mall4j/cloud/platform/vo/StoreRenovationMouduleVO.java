package com.mall4j.cloud.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("组件查询返回参数")
public class StoreRenovationMouduleVO {
    @ApiModelProperty("微页面名称")
    private String name ;
    @ApiModelProperty("id")
    private  Long renovationId;
    @ApiModelProperty("页面类型 0-微页面，1 -主页，2-底部导航，3-分类页，4-会员主页")
    private Integer homeStatus;
    @ApiModelProperty("0-未启动，1-已启用")
    private Integer status;
    @ApiModelProperty("适用门店")
    private List<Long> renoApplyStoreList;
}
