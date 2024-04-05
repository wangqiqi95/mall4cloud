package com.mall4j.cloud.group.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "group_store")
public class GroupStore {

    @ApiModelProperty("主键id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("拼团活动id")
    private Long groupActivityId;
    @ApiModelProperty("门店id")
    private Long storeId;

}
