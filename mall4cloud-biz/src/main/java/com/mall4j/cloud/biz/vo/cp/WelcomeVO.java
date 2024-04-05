package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 欢迎语配置表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WelcomeVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("是否是全部商店")
    private Integer  isAllShop;

    @ApiModelProperty("  1 已删除 0 未删除")
    private Integer flag;

    @ApiModelProperty("关联门店数")
    private List<ShopWelcomeConfig> shops;
    @ApiModelProperty("附件类型")
    private String attachmentType;
    @ApiModelProperty("创建人名称")
    private String createName;
    @ApiModelProperty("场景 0未注册，1注册")
    private String scene;

	@Override
	public String toString() {
		return "WelcomeVO{" +
				"id=" + id +
				",title=" + title +
				",weight=" + weight +
				",slogan=" + slogan +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
