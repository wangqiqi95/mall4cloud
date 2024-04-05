package com.mall4j.cloud.api.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户群表VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustGroupVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("群id")
    private String id;

    @ApiModelProperty("群名称")
    private String groupName;

    @ApiModelProperty("群主id staffId")
    private Long ownerId;

    @ApiModelProperty("建群时间")
    private Date createTime;

    @ApiModelProperty("群主所在店")
    private Long storeId;

    @ApiModelProperty("群二维码")
    private String qrCode;

    @ApiModelProperty("总客户数")
    private Long totalCust;

    @ApiModelProperty("总人数")
    private Integer total;

    private Integer totalLimit;

    private String userId;

    @ApiModelProperty("过期时间")
    private Date expireDate;
    /**
     *
     */
    private Date updateTime;

    /**
     * 客户群跟进状态: 0 - 跟进人正常 1 - 跟进人离职 2 - 离职继承中 3 - 离职继承完成
     */
    private Integer status;

    /**
     *
     */
    private Integer flag;


    private String ownerName;
    /**
     *
     */
    private String storeName;

}
