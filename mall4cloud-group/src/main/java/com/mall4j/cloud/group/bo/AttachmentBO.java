package com.mall4j.cloud.group.bo;

import com.mall4j.cloud.group.vo.PopUpAdAttachmentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttachmentBO {

    private Long popUpAdId;

    private String AttachmentType;

    private List<PopUpAdAttachmentVO> popUpAdAttachmentVOList;

    private Integer autoOffSeconds;

    private LocalDateTime activityEndTime;

}
