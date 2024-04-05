package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信菜单表VO
 *
 * @author gmq
 * @date 2022-01-26 23:14:17
 */
@Data
public class WeixinMenuTreeVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private String id;

    @ApiModelProperty("菜单KEY")
    private String menuKey;

    @ApiModelProperty("菜单类型")
    private String menuType;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("相应消息类型")
    private String msgtype;

    @ApiModelProperty("菜单位置")
    private Integer orders;

    @ApiModelProperty("公众号原始id")
    private String appId;

    @ApiModelProperty("公众号原始id")
    private String url;

	@ApiModelProperty("子级菜单")
	private List<WeixinMenuTreeChildVO> menuChilds;

}
