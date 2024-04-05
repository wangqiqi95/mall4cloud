package com.mall4j.cloud.user.dto;


import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class QueryGroupPushTaskPageDetailDTO extends PageDTO {

    @ApiModelProperty("接受者昵称")
    private String userName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("0-待发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer sendStatus;

}
