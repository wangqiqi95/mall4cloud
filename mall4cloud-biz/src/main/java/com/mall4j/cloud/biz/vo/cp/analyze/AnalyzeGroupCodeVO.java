package com.mall4j.cloud.biz.vo.cp.analyze;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class AnalyzeGroupCodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("群名称")
    private String name;

    @ApiModelProperty("群码")
    private String qrCode;

    @ApiModelProperty("状态: 0无效 1有效")
    private Integer status;

    @ApiModelProperty("启用停用判断状态: 0无效 1有效")
    private Integer enableStatus;

    @ApiModelProperty("扫码入群人数")
    private Integer scanCount;

    @ApiModelProperty("人数上限")
    private Integer upperTotal;

    private Date expireEnd;

    /**
     * 群实际群人数
     */
    private Integer chatUserTotal=0;

}
