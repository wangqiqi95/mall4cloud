package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "抽奖接口结果实体")
public class DrawPrizeVO implements Serializable {
    private String prizeName;
    private String prizeImgs;
}
