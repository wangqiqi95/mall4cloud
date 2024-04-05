package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人欢迎语 分时段欢迎语列表
 *
 * @author FrozenWatermelon
 * @date 2023-11-06 17:02:34
 */
@Data
public class CpWelcomeTimeState extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("欢迎语主表id")
    private Long welId;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("时间段：周期")
    private String timeCycle;

    @ApiModelProperty("时间段：开始时间")
    private String timeStart;

    @ApiModelProperty("时间段：结束时间")
    private String timeEnd;

}
