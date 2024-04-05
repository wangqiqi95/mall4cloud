package com.mall4j.cloud.group.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QueryPopUpAdPageDTO extends PageDTO {

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "内容类型：IMAGE图片广告，VIDEO视频，COUPON优惠券，QUESTIONNAIRE问卷")
    private String attachmentType;

    @ApiModelProperty(value = "活动名称阶段，1未开始， 2进行中，3已结束")
    private Integer stage;

    @ApiModelProperty(value = "状态 0未启用 1已启用")
    private Boolean status;

    @ApiModelProperty(value = "可用门店列表")
    private List<Long> storeIdList;

}
