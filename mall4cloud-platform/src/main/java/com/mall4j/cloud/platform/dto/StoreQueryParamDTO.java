package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("门店查询参数")
public class StoreQueryParamDTO {
    @ApiModelProperty("门店位置id")
    private Long cityId;
    @ApiModelProperty("门店类型：18-正品仓,20-虚拟仓,23-次品仓,27-退货仓,21-BUFFER仓,29-寄卖仓,32-经销店,34-自营店,26-过季仓,43-其他店,68-报废仓,25质检仓,19-客户仓,48-电商店,9-店铺（作废）,30-下单地址,31-结算仓,68-报废店,28-样板仓,53-配比仓,58-物料仓,63-代销店,33-代销仓,22-中转仓")
    private String storeType;
    @ApiModelProperty(value = "门店性质 自营/经销/代销/电商/其他")
    private String storenature;
    @ApiModelProperty("门店名称/编码")
    private String keyword;
    @ApiModelProperty("门店状态 0:关闭 1:营业")
    private Integer storeStatus;
    @ApiModelProperty("邀请码状态 0-禁用 1-启用")
    private Integer inviteStatus;
    @ApiModelProperty("片区节点id集合")
    private List<Long> districtOrgIdList;
    @ApiModelProperty("店群节点id 集合")
    private List<Long>  groupOrgIdList;
    @ApiModelProperty("已存在门店集合")
    private List<Long> existedStoreIdList ;

    @ApiModelProperty("限制门店集合")
    private List<Long> limitOrgIdList ;

    @ApiModelProperty("门店集合")
    private List<Long> storeIdList;

    @ApiModelProperty("标签集合")
    private List<Long> tagIdList;

    @ApiModelProperty("门店编号集合")
    private List<String> storeCodeList;

    @ApiModelProperty("限制节点/门店集合")
    private List<Long> limitOrgIdStoreIdList;

    @ApiModelProperty("开启电子价签同步：0否 1是")
    private Integer pushElStatus;

    @ApiModelProperty("c端是否展示 0-不展示 ，1 -展示")
    private Integer isShow;

    @ApiModelProperty("slc状态：全套-待客户反馈审批/转店-待SVP审批/关店-待Kids总经理审批/转店-待财务审批/无效的/全套-待经销商/商场反馈审批/平面-待客户反馈审批/已删除/已关店/待转店/转店-待登记ERP代码/待开店登记/待SVP审批/待关店/待提交全套图/平面-待客户审批/待分配设计师/待采购下单/转店-待总经理审批/全套-待区域设计经理审批/暂停营业/全套-待经销商审批/未通过/道具增补-待店筹审批/待登记ERP代码/待编制预算/关店-待SVP审批/平面-待区域设计经理审批/平面-待经销商/商场反馈审批/已转店/已暂停/待完善装修资料/待提交平面图/待验收完成/平面-待区域总监审批/平面-待经销商审批/关店-待省总审批/关店-待区总审批/待财务审批/草稿状态/关店-待AVP审批/运营中/平面-待区总审批/全套-待客户审批/开店申请-草稿(经销商)/关店-待财务审批/平面-陈列套餐变更调整-待审批/升级-待客户编辑")
    private String slcName;

    private List<String> slcNameList;

    @ApiModelProperty("是否虚拟门店：0否 1是")
    private Integer storeInviteType;
}
