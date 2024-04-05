package com.mall4j.cloud.api.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class CRMUserTagDto {

    private List<String> unionIds;

    private List<String> originIds;
}
