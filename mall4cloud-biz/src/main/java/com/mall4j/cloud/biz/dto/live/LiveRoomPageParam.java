

package com.mall4j.cloud.biz.dto.live;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * @author chaoge
 */
@Data
public class LiveRoomPageParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "直播间名称", required = true)
    private String name;

    @ApiModelProperty(value = "主播昵称", required = true)
    private String anchorName;

    @ApiModelProperty(value = "是否置顶 1.是 0.不是")
    private Integer roomTop;

    @ApiModelProperty(value = "直播间状态 101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期")
    private Integer liveStatus;

    @ApiModelProperty(value = "直播开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "直播结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
