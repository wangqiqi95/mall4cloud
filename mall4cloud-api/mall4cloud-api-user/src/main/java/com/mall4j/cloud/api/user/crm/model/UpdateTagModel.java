package com.mall4j.cloud.api.user.crm.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTagModel {

    @ApiModelProperty("外部联系人userId")
    private List<String> qiWeiUserIds;

    @ApiModelProperty("外部联系人unionId")
    private List<String> qiWeiUserUnionIds;

    @ApiModelProperty("员工userId")
    private String staffUserId;

    /**
     * 数据格式
     * [
     *   {
     *     "tagId": 1,
     *     "tagName": "123",
     *     "tagValues": [
     *       "123"
     *     ]
     *   }
     * ]
     */
    @ApiModelProperty("标签信息")
    private String tags;

}

