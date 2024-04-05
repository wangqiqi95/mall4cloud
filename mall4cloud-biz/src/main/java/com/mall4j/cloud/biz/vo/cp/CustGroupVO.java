package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 客户群表VO
 *
 * @author hwy
 * @date 2022-02-16 09:29:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustGroupVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("群id")
    private String id;

    @ApiModelProperty("群名称")
    private String groupName;

    @ApiModelProperty("群公告")
    private String notice;

    @ApiModelProperty("群主名称")
    private String ownerName;

    @ApiModelProperty("群主id")
    private Long ownerId;

    @ApiModelProperty("建群时间")
    private Date createTime;

    @ApiModelProperty("群二维码")
    private String qrCode;

    @ApiModelProperty("总客户数")
    private Long totalCust;

    @ApiModelProperty("总人数")
    private Integer total;

    private Integer totalLimit;

    private String userId;

    @ApiModelProperty("员工总人数")
    private Integer totalStaffCust;

    @ApiModelProperty("拉群方式：0企微群活码/1自建群活码")
    private Integer groupType;

	@ApiModelProperty("标签组")
	private List<Tag> tagList;

    @ApiModelProperty("过期时间")
    private Date expireDate;
    /**
     *
     */
    private Date updateTime;

    private Integer status;

    /**
     *客户群跟进状态:
     * 0 - 跟进人正常
     * 1 - 跟进人离职
     * 2 - 离职继承中
     * 3 - 离职继承完成
     */
    private Integer adminStatus;

    /**
     *
     */
    private Integer flag;



}
