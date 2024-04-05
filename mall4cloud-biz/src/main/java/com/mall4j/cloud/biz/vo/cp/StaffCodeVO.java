package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工活码表VO
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffCodeVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("活动标题")
    private String codeName;

    @ApiModelProperty("活码类型 1 批量单人 2 单人 3 多人 ")
    private Integer codeType;

    @ApiModelProperty("欢迎语")
    private String slogan;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("状态   0 无效  1  有效")
    private Integer status;

    @ApiModelProperty("通过好友方式 0 自动  1 手动")
    private Integer  authType;

    @ApiModelProperty("  1 已删除 0 未删除")
    private Integer flag;
    @ApiModelProperty("  标签")
    private String tags;

    @ApiModelProperty("员工姓名")
    private  String staffName;

    public void setTags(String tags){
       this.tags = tags;
    }
    @ApiModelProperty("创建人名称")
    private String createName;

    @ApiModelProperty("二维码链接")
    private  String qrCode;

    @ApiModelProperty("创建渠道 0 后台 1 小程序")
    private  Integer origin;


	@Override
	public String toString() {
		return "WelcomeVO{" +
				"id=" + id +
				",slogan=" + slogan +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",status=" + status +
				",flag=" + flag +
				'}';
	}
}
