package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.dto.cp.CpGroupCodeListDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeRef;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 自动拉群活码表VO
 *
 * @author gmq
 * @date 2023-10-27 16:59:32
 */
@Data
public class CpAutoGroupCodeVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("活码名称")
    private String codeName;

    @ApiModelProperty("活码类型")
    private Integer codeType;

    @ApiModelProperty("标签集合")
    private String tags;

    @ApiModelProperty("通过好友：0 自动通过， 1 手动通过")
    private Integer authType;

    @ApiModelProperty("")
    private Date createTime;

    @ApiModelProperty("")
    private Long createBy;

    @ApiModelProperty("")
    private String createName;

    @ApiModelProperty("")
    private Date updateTime;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("1 已删除 0 未删除")
    private Integer flag;

    @ApiModelProperty("")
    private String configId;

    @ApiModelProperty("")
    private String state;

    @ApiModelProperty("二维码链接")
    private String qrCode;

    @ApiModelProperty("自动备注：0否/1是")
    private Integer autoRemarkState;

    @ApiModelProperty("二维码样式")
    private String codeStyle;

    @ApiModelProperty("自动备注前缀")
    private String autoRemark;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("引流链接")
    private String drainageUrl;

    @ApiModelProperty("引流页面")
    private String drainagePath;

    @ApiModelProperty("拉群方式：0企微群活码/1自建群活码")
    private Integer groupType;

    @ApiModelProperty("分组id")
    private Long groupId;

    @ApiModelProperty("")
    private String updateBy;

    @ApiModelProperty("已添加好友数")
    private Integer addUserCount;

    @ApiModelProperty("执行员工")
    private List<CpAutoGroupCodeStaffVO> staffs;

    @ApiModelProperty("邀请人数")
    private Integer inviteCount;
    @ApiModelProperty("入群人数")
    private Integer joinGroupCount;

    @ApiModelProperty(value = "群组集合",required = true)
    private List<CpGroupCodeRef> codeList;
}
