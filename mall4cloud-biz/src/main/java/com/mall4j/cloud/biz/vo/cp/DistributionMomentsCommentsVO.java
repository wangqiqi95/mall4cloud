package com.mall4j.cloud.biz.vo.cp;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 朋友圈互动明细数据VO
 *
 * @author gmq
 * @date 2024-03-04 16:47:40
 */
@Data
public class DistributionMomentsCommentsVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("")
    private String userId;

	@ApiModelProperty("")
	private String userName;

	@ApiModelProperty("")
	private String cpRemark;

    @ApiModelProperty("")
    private Long staffId;

    @ApiModelProperty("")
    private String staffUserId;

    @ApiModelProperty("")
    private String momentsId;

    @ApiModelProperty("")
    private String createBy;

    @ApiModelProperty("")
    private String updateBy;

	@ApiModelProperty("0评论/1点赞")
	private Integer type;

}
