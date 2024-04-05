package com.mall4j.cloud.biz.model.cp;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mall4j.cloud.biz.dto.cp.MaterialMsgDTO;
import com.mall4j.cloud.biz.wx.cp.constant.MaterialMsgType;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材表
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Material extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 素材名称
     */
    private String matName;

    /**
     * 素材分类id
     */
    private Integer matType;

    /**
     * 素材内容
     */
    private String matContent;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createBy;
	/**
	 * 创建人
	 */
	private String createName;
	private Integer  isAllShop;
    /**
     * 是否删除
     */
    private Integer flag;
	/**
	 * 开始时间(素材有效期)
	 */
	private Date validityCreateTime;
	/**
	 * 截止时间(素材有效期)
	 */
	private Date validityEndTime;
	/**
	 * 是否置顶，0否 1是
	 */
	private Integer isTop;

	/**
	 * 累计访客数
	 */
	private Integer visitorCount;

	/**
	 * 累计浏览数
	 */
	private Integer browseCount;

	/**
	 * 是否开启互动雷达 0否1是
	 */
	private Integer interactiveRadar;

	private String matLabal;

	@ApiModelProperty("使用次数")
	private Integer useNum;

	@ApiModelProperty("话术内容")
	private String scriptContent;


    public void setContent(List<MaterialMsgDTO> msgList) {
		Set<String> set = new HashSet();
		StringBuilder content = new StringBuilder(10);
		msgList.forEach(item-> {
			MaterialMsgType msgTypeEnum = MaterialMsgType.get(item.getType());
			set.add(msgTypeEnum!=null?msgTypeEnum.getTxt():"");
		});

		set.forEach(typeName ->{
			content.append(typeName).append("+");
		});
		this.setMatContent(content.substring(0,content.length()-1));
    }
}
