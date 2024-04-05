package com.mall4j.cloud.group.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 问卷 会员答题记录内容DTO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public class QuestionnaireUserAnswerRecordContentDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("内容")
    private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "QuestionnaireUserAnswerRecordContentDTO{" +
				"id=" + id +
				",content=" + content +
				'}';
	}
}
