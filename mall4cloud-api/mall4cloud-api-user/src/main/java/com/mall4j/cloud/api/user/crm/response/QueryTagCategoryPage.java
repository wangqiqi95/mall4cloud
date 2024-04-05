package com.mall4j.cloud.api.user.crm.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.http.ssl.PrivateKeyStrategy;

import java.util.List;

@Data
public class QueryTagCategoryPage {
    @ApiModelProperty("标签id")
    private String id;
    @ApiModelProperty("标签名称")
    private String name;
    @ApiModelProperty("标签描述")
    private String description;
    @ApiModelProperty("标签来源")
    private String origin;

    @ApiModelProperty("//标签类型\n" +
            "    //WITHIN_RULE,规则标签\n" +
            "    //MANUAL,手动\n" +
            "    //EXTERNAL，外部\n" +
            "    //ENUM_VALUE  枚举")
    private String type;
    @ApiModelProperty("//标签值类型\n" +
            "    //HAVE_VALUE, 枚举类型值标签HAVE_MULTI_ENUM_VALUE,值标签-非固定枚举值 HAVE_MULTI_NUMBER_VALUE, 值标签-非固定数值 HAVE_MULTI_STRING_VALUE, 值标签-非固定字符串 HAVE_MULTI_DATE_VALUE, 值标签-非固定日期\n" +
            "    //NO_VALUE 无值")
    private String tagValueType;
    @ApiModelProperty("标签下客户数")
    private Integer customerCount;
    @ApiModelProperty("//更新策略\n" +
            "    //ONLY_ONCE,\n" +
            "    //MANUAL,\n" +
            "    //AUTO,\n" +
            "    //MANUAL_TAG")
    private String updatePolicy;
    @ApiModelProperty("//标签状态\n" +
            "    //DELETED, 删除\n" +
            "    //OFFLINE, 下线\n" +
            "    //SUBMITTING,上线中\n" +
            "    //WAIT_RETRY, 稍后重试SUBMIT_SUCCESSFUL, 未计算 SUBMIT_FAILED, 上线失败NOT_CALCULATE, 未计算WAIT_CALCULATE, 待计算CALCULATING, 计算中CALCULATE_SUCCESSFUL, 计算完成\n" +
            "    //CALCULATE_FAILED 计算失败")
    private String status;
    @ApiModelProperty("标签分组id")
    private Integer categoryId;
    @ApiModelProperty("外部标签id")
    private String externalTagId;
    @ApiModelProperty("手动模型")
    private String manualTagFqn;
    @ApiModelProperty("有值枚举字符串的取值范围")
    private List<String> charValues;

}
