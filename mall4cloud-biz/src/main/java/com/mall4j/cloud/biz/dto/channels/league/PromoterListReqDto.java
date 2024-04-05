package com.mall4j.cloud.biz.dto.channels.league;

import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 达人列表入参
 * @Author axin
 * @Date 2023-02-14 16:45
 **/
@Data
public class PromoterListReqDto extends PageDTO {
    @ApiModelProperty(value = "视频号名称")
    private String finderName;

    @ApiModelProperty(value = "门店列表")
    private List<Long> storeIds;

    @ApiModelProperty(value = "0 初始值 " +
            "1 邀请中" +
            "2 达人已接受邀请 " +
            "3 达人已拒绝邀请" +
            "4 已取消邀请" +
            "5 已取消合作" +
            "10 已删除" +
            "返回码")
    private Integer status;

    @ApiModelProperty(value = "视频号id列表")
    private List<String> finderIds;
}
