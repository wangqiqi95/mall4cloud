package com.mall4j.cloud.biz.mapper;

import com.mall4j.cloud.biz.model.WeixinMaSubscriptUserBussinessRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分礼品到货提醒记录关联
 * @author Grady_Lu
 */
public interface WeixinMaSubscriptUserBussinessRecordMapper {

    void save(@Param("bussinessRecord") WeixinMaSubscriptUserBussinessRecord bussinessRecord);

    List<WeixinMaSubscriptUserBussinessRecord> getUserBusRecordByUserRecordId(@Param("userRecordId") Long userRecordId);

    /**
     * 根据用户id和业务id集合查询记录关联表数据
     * @param userId
     * @param bussinessIdList
     * @return
     */
    List<WeixinMaSubscriptUserBussinessRecord> getUserRecordByUserIdAndBusIds(@Param("userId") Long userId, @Param("bussinessIdList") List<String> bussinessIdList);
}
