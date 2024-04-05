package com.mall4j.cloud.api.biz.dto.crm;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CpCrmGroup implements Serializable {
    private static final long serialVersionUID = -104908521674072418L;

    private String chatId;
    private String name;
    private String owner;
    private String notice;
    private Long created;
    private String change_type;

    private List<CpCrmGroupMember> member_list;
}
