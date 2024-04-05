package com.mall4j.cloud.user.bo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class BatchWrapperTaskStaffAndVipBO {

    List<AddGroupPushTaskVipRelationBO> relationBOList;

//    Set<AddGroupPushTaskStaffRelationBO> staffRelationBOSet;

    Map<Long, AddGroupPushTaskStaffRelationBO> taskStaffMap = new HashMap<>();
}
