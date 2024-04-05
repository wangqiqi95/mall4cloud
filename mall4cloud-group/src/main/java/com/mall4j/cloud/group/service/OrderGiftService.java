package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.model.OrderGift;
import com.mall4j.cloud.group.vo.OrderGiftListVO;

import java.util.List;

public interface OrderGiftService extends IService<OrderGift> {
    List<OrderGiftListVO> orderGiftList(OpenScreenAdPageDTO param);

    OrderGift selectFirstActivity(OrderGiftInfoAppDTO param);

}
