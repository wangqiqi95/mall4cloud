package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

/**
 * 问卷表单内容
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@Data
public class QuestionnaireForm implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 表单内容
     */
    private String content;
	private String formNames;

}
