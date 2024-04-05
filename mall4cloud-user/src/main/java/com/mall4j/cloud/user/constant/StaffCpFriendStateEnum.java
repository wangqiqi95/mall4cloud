package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum StaffCpFriendStateEnum {

    YES(1),
    NO(0);

    private Integer friendState;

    StaffCpFriendStateEnum(Integer friendState){
        this.friendState = friendState;
    }

}
