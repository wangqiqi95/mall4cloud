package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 统计好友助力
 * @Author axin
 * @Date 2022-10-18 10:42
 **/
@Data
public class CountFriendAssistanceRespDto {
    @ApiModelProperty("已邀请人数")
    private Integer inviteesNum;
    @ApiModelProperty("累计积分")
    private Integer scoreSum;
}
