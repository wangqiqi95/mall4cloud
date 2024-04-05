package com.mall4j.cloud.order.dto;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.common.exception.Assert;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class OrderDetailReportRequest {

    @ApiModelProperty("开始时间 yyyy-MM-dd")
    @NotBlank(message = "开始时间不能为空")
    private String beginTime;

    @ApiModelProperty("结束时间 yyyy-MM-dd")
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty("间隔天数")
    private Integer dayCount;

    @ApiModelProperty("返回数据时间格式，0按小时返回，1按天返回")
    private Integer dateType = 0;

    @ApiModelProperty("门店集合")
    private List<Long> storeIds;

    @ApiModelProperty(value = "门店集合",hidden = true)
    private Long staffId;

    public void check(){
        if(this.dateType == 1){
            Assert.isNull(dayCount,"数据按天返回时，间隔天数不允许为空。");
        }
        if(CollUtil.isEmpty(this.storeIds)){
            Assert.faild("门店ids不允许为空。");
        }
    }


}
