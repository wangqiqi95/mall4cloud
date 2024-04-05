package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工活码表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffCodePlusVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("活动标题")
    private String codeName;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建人名称")
    private String createName;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("通过好友方式 0 自动  1 手动")
    private Integer  authType;

    @ApiModelProperty("  标签")
    private String tags;

    @ApiModelProperty("二维码链接")
    private  String qrCode;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("引流链接")
    private String drainageUrl;

    @ApiModelProperty("引流页面")
    private String drainagePath;

    @ApiModelProperty("渠道关联id")
    private Long codeChannelId;

    /**
     * 自动描述：0否/1是
     */
    @ApiModelProperty("0否/1是")
    @NotNull(message = "自动描述不能为空")
    private Integer autoDescriptionState;

    /**
     * 自动描述前缀
     */
    @ApiModelProperty("自动描述注前缀")
    private String autoDescription;

}
