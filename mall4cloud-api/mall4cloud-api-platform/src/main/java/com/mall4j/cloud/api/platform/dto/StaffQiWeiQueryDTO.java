package com.mall4j.cloud.api.platform.dto;

import com.mall4j.cloud.common.database.dto.PageDTO;
import lombok.Data;

/**
 * @author zhangjie
 */
@Data
public class StaffQiWeiQueryDTO extends PageDTO {

    private Long storeId;
    private String roleType;
    private String staffName;
    private String qwStatus;
    private Integer storeStatus;
}
