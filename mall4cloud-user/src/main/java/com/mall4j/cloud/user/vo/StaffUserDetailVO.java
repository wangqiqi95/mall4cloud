package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.api.user.vo.UserApiVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffUserDetailVO {

    @ApiModelProperty("会员详情")
    private UserApiVO userDetail;
    @ApiModelProperty("会员数据")
    private UserInfoVO userInfo;

}
