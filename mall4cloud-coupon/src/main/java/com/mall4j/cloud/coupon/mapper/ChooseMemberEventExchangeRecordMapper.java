package com.mall4j.cloud.coupon.mapper;

import com.mall4j.cloud.coupon.dto.ChooseMemberEventExchangeRecordPageDTO;
import com.mall4j.cloud.coupon.model.ChooseMemberEventExchangeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.vo.ChooseMemberEventExchangeRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventExchangeRecordMapper extends BaseMapper<ChooseMemberEventExchangeRecord> {

    Integer getTheUserEventExchangeNum(@Param("userId") Long userId, @Param("eventId") Long eventId);

    List<ChooseMemberEventExchangeRecordVO> recordList(@Param("pageDTO") ChooseMemberEventExchangeRecordPageDTO pageDTO);

    Integer getExchangeMemberCountByEvent(@Param("eventId") Long eventId);

    void confirmExport(@Param("ids") List<Long> ids);

    List<ChooseMemberEventExchangeRecordVO> queryUserRecord(@Param("userId") Long userId);
}
