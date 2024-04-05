package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.PayActivityPageDTO;
import com.mall4j.cloud.group.model.PayActivity;
import com.mall4j.cloud.group.vo.PayActivityListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayActivityMapper extends BaseMapper<PayActivity> {
    List<PayActivityListVO> payActivityList(PayActivityPageDTO param);

    PayActivity selectFirstActivity(@Param("shopId") String shopId);
}
