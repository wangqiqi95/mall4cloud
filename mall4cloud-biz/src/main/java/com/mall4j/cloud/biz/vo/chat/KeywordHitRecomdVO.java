package com.mall4j.cloud.biz.vo.chat;

import com.mall4j.cloud.biz.model.chat.KeyCustom;
import com.mall4j.cloud.biz.vo.cp.CpChatScriptVO;
import com.mall4j.cloud.biz.vo.cp.MiniMaterialVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 关键词VO
 *
 */
@Data
public class KeywordHitRecomdVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("敏感词")
    private String sensitives;

    @ApiModelProperty("匹配词")
    private String mate;

    @ApiModelProperty(value = "话术id")
    private String speechcraftId;

    @ApiModelProperty(value = "素材id")
    private String materialId;

    @ApiModelProperty(value = "素材内容")
    private List<MiniMaterialVO> materials;

    @ApiModelProperty(value = "话术内容")
    private List<CpChatScriptVO> scripts;
}
