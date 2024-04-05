package com.mall4j.cloud.user.event;

import com.mall4j.cloud.user.dto.UserManagerDTO;
import lombok.Data;

/**
 * @Date 2022年4月29日, 0029 17:03
 * @Created by eury
 */
@Data
public class SoldUsersEvent {

    private UserManagerDTO userManagerDTO;

    private Long downLoadHisId;
}
