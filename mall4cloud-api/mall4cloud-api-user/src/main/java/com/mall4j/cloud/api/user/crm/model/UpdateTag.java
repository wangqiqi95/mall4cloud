package com.mall4j.cloud.api.user.crm.model;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTag {

    private String tagId;
    private String tagName;
    private List<String> tagValues;

}

