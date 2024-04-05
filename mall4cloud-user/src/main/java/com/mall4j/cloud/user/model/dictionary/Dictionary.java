package com.mall4j.cloud.user.model.dictionary;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ben
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_dictionary")
@ApiModel(value="Dictionary对象", description="")
public class Dictionary extends Model<Dictionary> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典ID")
    @TableId(value = "dictionary_id", type = IdType.AUTO)
    private Long dictionaryId;

    @ApiModelProperty(value = "字典类型")
    private String dictionaryType;

    @ApiModelProperty(value = "字典描述")
    private String dictionaryDescription;

    @ApiModelProperty(value = "字典key")
    private String dictionaryKey;

    @ApiModelProperty(value = "字典值")
    private String dictionaryValue;

    @ApiModelProperty(value = "字典备注")
    private String dictionaryRemark;

    @ApiModelProperty(value = "创建者ID")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.dictionaryId;
    }

}
