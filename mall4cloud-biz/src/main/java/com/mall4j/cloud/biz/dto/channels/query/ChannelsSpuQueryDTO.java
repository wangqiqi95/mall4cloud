package com.mall4j.cloud.biz.dto.channels.query;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @date 2023/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "视频号4.0商品查询DTO")
public class ChannelsSpuQueryDTO extends PageDTO {

    @ApiModelProperty(value = "货号获取商品名称")
    private String codeOrName = "";

    @ApiModelProperty(value = "商品状态 0:初始值 2上架审核中 5:上架 6:回收站 11:自主下架 13:违规下架/风控系统下架;14:保证金违规下架；15:品牌到期下架; 20:封禁下架。")
    private String status = "";

    @ApiModelProperty(value = "0:初始值 1:编辑中 2:审核中 3:审核失败 4:审核成功 5:商品信息写入中")
    private String editStatus = "";

    @ApiModelProperty(value = "添加至橱窗 1是 0否")
    private Integer isWindow;
}
