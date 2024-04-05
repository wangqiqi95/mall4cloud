package com.mall4j.cloud.api.user.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserStaffRelUtils {

    /**
     * 根据员工企微userid+客户userid为key获取员工对应客户信息
     * @param list
     * @return
     */
    public static Map<String,UserStaffCpRelationListVO> mapByStaffAndUser(List<UserStaffCpRelationListVO> list){
        list=list.stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId()) && StrUtil.isNotEmpty(item.getQiWeiStaffId())).collect(Collectors.toList());
        if(CollUtil.isEmpty(list)){
            return MapUtil.empty();
        }
        Map<String,UserStaffCpRelationListVO> dataMap=new LinkedHashMap<>();
        for (UserStaffCpRelationListVO listVO : list) {
            String key=listVO.getQiWeiUserId()+listVO.getQiWeiStaffId();
            if(!dataMap.containsKey(key)){
                dataMap.put(key,listVO);
            }
        }
        return dataMap;
    }

    /**
     * 根据员工id+客户unionId为key获取员工对应客户信息
     * @param list
     * @return
     */
    public static Map<String,UserStaffCpRelationListVO> mapByStaffIdAndUnionId(List<UserStaffCpRelationListVO> list){
        list=list.stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId()) && StrUtil.isNotEmpty(item.getQiWeiStaffId())).collect(Collectors.toList());
        if(CollUtil.isEmpty(list)){
            return MapUtil.empty();
        }
        Map<String,UserStaffCpRelationListVO> dataMap=new LinkedHashMap<>();
        for (UserStaffCpRelationListVO listVO : list) {
            String key=listVO.getUserUnionId()+listVO.getStaffId();
            if(!dataMap.containsKey(key)){
                dataMap.put(key,listVO);
            }
        }
        return dataMap;
    }

}
