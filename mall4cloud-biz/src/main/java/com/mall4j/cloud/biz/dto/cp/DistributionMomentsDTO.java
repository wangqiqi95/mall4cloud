package com.mall4j.cloud.biz.dto.cp;

import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsStore;
import com.mall4j.cloud.common.database.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-朋友圈DTO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Data
public class DistributionMomentsDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("可见开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("可见结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("头像地址")
    private String avatarUrl;

    @ApiModelProperty("文案")
    private String descHtml;

    @ApiModelProperty("评论")
    private String comment;

//    @ApiModelProperty("选择商品类型 0全部商品 1部分商品")
//    private Integer productType;

    @ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

//    @ApiModelProperty("分销类型 0全部 1导购 2威客")
//    private Integer distributionType;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("朋友圈门店集合")
    private List<DistributionMomentsStore> storeList;

    @ApiModelProperty("朋友圈任务状态：0草稿/1进行中/2已终止/3超时终止")
    private Integer sendStatus;


    @ApiModelProperty("附件列表")
    private List<AttachMentBaseDTO>  attachMentBaseDTOS;
//    @ApiModelProperty("朋友圈商品集合")
//    private List<DistributionMomentsProduct> productList;

    @ApiModelProperty("批量更新状态 id集合")
    private List<Long> ids;

    @ApiModelProperty("列表查询门店ID")
    private Long queryStoreId;

    @ApiModelProperty("列表查询 门店集合")
    private List<Long> queryStoreIds;

    @ApiModelProperty("是否置顶 1是 0否")
    private Integer top;

    private Long currentUserId;

    @ApiModelProperty("附件")
    private List<CpWelcomeAttachment> attachment;
}
