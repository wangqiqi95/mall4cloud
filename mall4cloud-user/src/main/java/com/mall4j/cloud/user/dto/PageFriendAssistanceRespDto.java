package com.mall4j.cloud.user.dto;

import com.mall4j.cloud.user.vo.UserScoreLogVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 助力明细列表
 * @Author axin
 * @Date 2022-10-18 15:41
 **/
@Data
public class PageFriendAssistanceRespDto  {
    @ApiModelProperty("日志id")
    private Long logId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("积分类型 12邀请新用户 13旧用户助力")
    private Integer source;

    @ApiModelProperty("变动积分数额")
    private Long score;

    @ApiModelProperty("业务id")
    private Long bizId;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("头像")
    private String pic;
}
