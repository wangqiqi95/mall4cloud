package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.user.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2022/01/09
 */
@Data
public class StaffUserVo {

    @ApiModelProperty("统计类型 1日 2周 3月")
    private Integer countType;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("总会员数量")
    private Integer totalNum;

    @ApiModelProperty("招募会员列表")
    private List<User> userList;

    @ApiModelProperty("今日会员数量")
    private Integer todayNum;

    @ApiModelProperty("本月会员数量")
    private Integer monthNum;

}
