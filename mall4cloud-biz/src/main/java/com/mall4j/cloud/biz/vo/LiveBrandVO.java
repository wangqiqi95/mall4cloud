package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * VO
 *
 * @author lt
 * @date 2022-01-17
 */
@Data
@ApiModel("视频号品牌")
public class LiveBrandVO extends BaseVO {

    @ApiModelProperty("品牌ID")
    private String brandId;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("审核状态 0:草稿,1:审核通过, 2:审核中, 9:审核拒绝")
    private Integer status;

    @ApiModelProperty("审核时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    @ApiModelProperty("拒绝原因")
    private String auditContent;
}
