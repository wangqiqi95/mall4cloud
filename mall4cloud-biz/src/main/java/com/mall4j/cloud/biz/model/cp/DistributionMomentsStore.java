package com.mall4j.cloud.biz.model.cp;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * 分销推广-朋友圈门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Data
public class DistributionMomentsStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 门店ID
     */
    @ApiModelProperty("部门/员工-id")
    private Long storeId;

	/**
	 * 0部门/1员工
	 */
    @ApiModelProperty("0部门/1员工")
	private Integer type;

}
