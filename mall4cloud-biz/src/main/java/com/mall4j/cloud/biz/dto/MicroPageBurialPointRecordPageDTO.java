package com.mall4j.cloud.biz.dto;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.database.dto.TimeBetweenPageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微页面埋点数据分页参数类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MicroPageBurialPointRecordPageDTO extends TimeBetweenPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 微页面ID
    * */
    @ApiModelProperty("微页面ID")
    private Long renovationId;

    /**
     * 客户昵称
     */
	@ApiModelProperty("客户昵称")
    private String nikeName;

    /**
     * 备注名称
     */
	@ApiModelProperty("备注名称")
    private String notesName;

    /**
     * 手机号
     */
	@ApiModelProperty("手机号")
    private String mobile;

    public void setNikeName(String nikeName) {
        if (StrUtil.isNotBlank(nikeName)) {
            this.nikeName = "%" + nikeName + "%";
        }
    }

    public void setNotesName(String notesName) {
        if (StrUtil.isNotBlank(notesName)) {
            this.notesName = "%" + notesName + "%";
        }
    }

    public void setMobile(String mobile) {
        if (StrUtil.isNotBlank(mobile)) {
            this.mobile = "%" + mobile + "%";
        }
    }
}
