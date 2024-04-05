package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.dto.cp.CpGroupCodeListDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeRef;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 群活码表VO
 *
 * @author hwy
 * @date 2022-02-16 15:17:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupCodeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    private String name;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建人名称")
    private String createName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人店id")
    private Long storeId;

    @ApiModelProperty("创建人店名称")
    private String storeName;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("  1 已删除 0 未删除")
    private Integer flag;

    @ApiModelProperty("")
    private String qrCode;

    @ApiModelProperty("")
    private String state;

    @ApiModelProperty("")
    private String configId;

    @ApiModelProperty("分组id")
    private Long groupId;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("拉群方式：0企微群活码/1自建群活码/2企微群活码基础")
    private Integer groupType;

    @ApiModelProperty("备用员工id")
    private Long standbyStaffId;

    @ApiModelProperty("活码样式")
    private String style;

    @ApiModelProperty("")
    private String updateBy;

    @ApiModelProperty("群活码源：0群活码/1自动拉群-群活码")
    private Integer codeFrom;

    @ApiModelProperty("引流地址")
    private String drainageUrl;

    @ApiModelProperty("活码地址")
    private String basicsCodeUrl;

    @ApiModelProperty("引流页面")
    private String drainagePath;

    @ApiModelProperty("关联数量")
    private Integer groupTotal;

    @ApiModelProperty("剩余可用群数")
    private Integer enabledGroupTotal;

    @ApiModelProperty("剩余可邀请客户数")
    private Integer enabledCustTotal;

    @ApiModelProperty("群客户数")
    private Integer custTotal;

    @ApiModelProperty(value = "群组集合")
    private List<CpGroupCodeRef> codeList;
}
