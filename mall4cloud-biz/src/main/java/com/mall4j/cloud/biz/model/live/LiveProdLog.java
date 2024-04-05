
package com.mall4j.cloud.biz.model.live;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LGH
 * @date 2020-08-04 16:57:17
 */
@Data
@ApiModel("直播商品日志")
public class LiveProdLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播商品日志id，这个表主要用来记录审核通过更新次数
     */
    @ApiModelProperty("直播商品日志id，这个表主要用来记录审核通过更新次数")
    @TableId
    private Long liveProdLogId;
    /**
     * 直播商品库id
     */
    @ApiModelProperty("直播商品库id")
    private Long liveProdStoreId;
    /**
     * 直播商品更新时间
     */
    @ApiModelProperty("直播商品更新时间")
    private Date updateTime;
    /**
     * -1:删除时间 0:商家创建商品 1:商家提交审核 2:审核通过 3:审核不通过 4:微信直播服务平台，违规下架 5:平台撤销直播商品
     */
    @ApiModelProperty("-1:删除时间 0:商家创建商品 1:商家提交审核 2:审核通过 3:审核不通过 4:微信直播服务平台，违规下架 5:平台撤销直播商品")
    private Integer status;
    /**
     * 微信提交审核后的商品ID
     */
    @ApiModelProperty("微信提交审核后的商品ID")
    private Integer goodsId;
    /**
     * 审核单Id
     */
    @ApiModelProperty("审核单Id")
    private Integer auditId;

}
