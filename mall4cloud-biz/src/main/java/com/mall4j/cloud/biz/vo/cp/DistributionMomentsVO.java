package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.DistributionMomentsStore;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-朋友圈VO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Data
public class DistributionMomentsVO extends BaseVO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("可见开始时间")
    private Date startTime;

    @ApiModelProperty("可见结束时间")
    private Date endTime;

    @ApiModelProperty("头像地址")
    private String avatarUrl;

    @ApiModelProperty("文案")
    private String descHtml;

    @ApiModelProperty("评论")
    private String comment;

    @ApiModelProperty("素材类型 1图片 2商品")
    private Integer materialType;

    @ApiModelProperty("图片素材地址  多个逗号分开")
    private String materialImgUrl;

    @ApiModelProperty("选择商品类型 0全部商品 1部分商品")
    private Integer productType;

    @ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

    @ApiModelProperty("适用门店数量")
    private Integer storeNum;

    @ApiModelProperty("适用门店Id集合")
    private List<Long> storeIds;

    @ApiModelProperty("适用部门/员工集合")
    private List<DistributionMomentsStore> storeList;

    @ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("朋友圈任务状态：0草稿/1进行中/2已终止/3超时终止")
    private Integer sendStatus;

    @ApiModelProperty("是否置顶 1是 0否")
    private Integer top;

    @ApiModelProperty("启用时间")
    private Date enableTime;

}
