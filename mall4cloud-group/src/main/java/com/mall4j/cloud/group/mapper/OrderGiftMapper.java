package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.model.OrderGift;
import com.mall4j.cloud.group.vo.OrderGiftListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderGiftMapper extends BaseMapper<OrderGift> {
    List<OrderGiftListVO> orderGiftList(OpenScreenAdPageDTO param);

    OrderGift selectFirstActivity(OrderGiftInfoAppDTO param);

}
