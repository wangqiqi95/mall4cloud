package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * admin用户查询种草列表入参
 *
 * @author cg
 */
@Data
public class RecommendAdminPageParamsDTO{

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("审核状态：不传值代表全部 | 1-待审核 | 2-审核通过 | 3-已驳回")
    private Integer status;

    @ApiModelProperty("种草分类id-不传值代表全部")
    private Integer recommendCateId;

    @ApiModelProperty("发布开始时间 格式 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("发布结束时间 格式 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
