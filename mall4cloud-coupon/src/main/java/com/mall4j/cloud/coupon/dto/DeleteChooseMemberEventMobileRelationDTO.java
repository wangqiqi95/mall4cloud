package com.mall4j.cloud.coupon.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class DeleteChooseMemberEventMobileRelationDTO {

    @Size( min = 1,max = 1000, message = "选择用户数量必须在1到1000的范围")
    @NotEmpty(message = "关系ID不能为空")
    public List<Long> relationIdList;

    @NotNull(message = "活动ID不允许为空")
    public Long eventId;
}
