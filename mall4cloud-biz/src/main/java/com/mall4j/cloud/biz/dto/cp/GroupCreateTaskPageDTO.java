package com.mall4j.cloud.biz.dto.cp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupCreateTaskPageDTO implements Serializable {

    private  String taskName;

    private String staffName;

    private List<Long> staffIds;

}
