package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 主推活动DTO
 *
 * @author EricJeppesen
 * @date 2022/10/18 13:56
 */
public class DistributionRecommendActivityStatusModifyDTO {

    /**
     * 主键标识
     */
    @ApiModelProperty("主键标识")
    @NotBlank(message = "主键标识不能为空")
    private Long id;
    /**
     * 活动状态
     */
    @ApiModelProperty("活动状态(1上线-0下线)")
    @NotBlank(message = "是否适用所有的产品不能为空")
    private Integer activityStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    @Override
    public String toString() {
        return "DistributionRecommendActivityStatusModifyDTO{" +
                "id=" + id +
                ", activityStatus=" + activityStatus +
                '}';
    }
}
