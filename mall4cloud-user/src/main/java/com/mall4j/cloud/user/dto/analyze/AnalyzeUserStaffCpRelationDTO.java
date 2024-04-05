package com.mall4j.cloud.user.dto.analyze;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnalyzeUserStaffCpRelationDTO {

    private String startTime;

    private String endTime;

    private String nickName;

    private String phone;

    private List<Long> staffs;

    private List<Long> codeChannelIds;

    private Long codeId;
}
