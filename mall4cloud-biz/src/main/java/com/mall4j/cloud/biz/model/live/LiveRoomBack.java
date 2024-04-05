

package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lhd
 * @date 2020-08-12 10:04:53
 */
@Data
@ApiModel("直播回放")
public class LiveRoomBack implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间回放信息
     */
    @ApiModelProperty("直播间回放信息")
    @TableId
    private Long backId;
    /**
     * 直播间id
     */
    @ApiModelProperty("直播间id")
    private Long roomId;
    /**
     * 回放视频url
     */
    @ApiModelProperty("回放视频url")
    private String mediaUrl;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private Date expireTime;

}
