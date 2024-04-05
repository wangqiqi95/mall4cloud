package com.mall4j.cloud.api.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterQiWeiMsgDTO implements Serializable {
    private  List<String> qiWeiStaffIdList ;
    private Long userId;

}
