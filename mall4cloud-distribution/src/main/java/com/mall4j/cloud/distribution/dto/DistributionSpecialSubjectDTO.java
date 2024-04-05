package com.mall4j.cloud.distribution.dto;

import com.mall4j.cloud.distribution.model.DistributionSubjectProduct;
import com.mall4j.cloud.distribution.model.DistributionSubjectStore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-分销专题DTO
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:56
 */
@Data
public class DistributionSpecialSubjectDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("微页面ID")
    private Long pageId;

    @ApiModelProperty("微页面名称")
    private String pageName;

    @ApiModelProperty("专题名称")
    private String subjectName;

    @ApiModelProperty("可见开始时间")
    private Date startTime;

    @ApiModelProperty("可见结束时间")
    private Date endTime;

    @ApiModelProperty("宣传图地址")
    private String publicityImgUrl;

    @ApiModelProperty("选择门店类型 0全部门店 1部分门店")
    private Integer storeType;

    @ApiModelProperty("选择商品类型 0全部商品 1部分商品")
    private Integer productType;

    @ApiModelProperty("宣传文案")
    private String publicityText;

    @ApiModelProperty("分销类型 0全部 1导购 2威客")
    private Integer distributionType;

    @ApiModelProperty("状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("是否自定义 1是 0否")
    private Integer isRec;

    @ApiModelProperty("推荐颜色")
    private String recColor;

    @ApiModelProperty("自定义颜色")
    private String customizeColor;

    @ApiModelProperty("专题门店列表")
    private List<DistributionSubjectStore> storeList;

    @ApiModelProperty("专题商品列表")
    private List<DistributionSubjectProduct> productList;

    @ApiModelProperty("批量更新状态 id集合")
    private List<Long> ids;

    @ApiModelProperty("列表查询门店ID")
    private Long queryStoreId;

    @ApiModelProperty("是否置顶 1是 0否")
    private Integer top;

    private Long currentUserId;

}
