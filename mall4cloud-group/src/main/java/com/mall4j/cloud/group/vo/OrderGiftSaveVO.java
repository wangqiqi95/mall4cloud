package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("下单赠品保存返回实体")
public class OrderGiftSaveVO implements Serializable {
    private List<String> failCommodityIds;
}
