package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MaterialBrowseStatisticsVO {

    @ApiModelProperty("累计浏览量")
    private Integer totalBrowseCount;
    @ApiModelProperty("访客数")
    private Integer totalVisitorCount;
    @ApiModelProperty("今日浏览量")
    private Integer todayBrowseCount;
    @ApiModelProperty("今日访客数")
    private Integer todayVisitorCount;

    @ApiModelProperty("浏览数据按天")
    private List<MaterialBrowseRecordByDayVO> browseRecordByDayVOS;
    @ApiModelProperty("访客数据按天")
    private List<MaterialBrowseRecordByDayVO> visitorRecordByDayVOS;

}
