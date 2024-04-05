package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.mapper.OrderGiftMapper;
import com.mall4j.cloud.group.model.OrderGift;
import com.mall4j.cloud.group.service.OrderGiftService;
import com.mall4j.cloud.group.vo.OrderGiftListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderGiftServiceImpl extends ServiceImpl<OrderGiftMapper, OrderGift> implements OrderGiftService {
    @Resource
    private OrderGiftMapper orderGiftMapper;
    @Override
    public List<OrderGiftListVO> orderGiftList(OpenScreenAdPageDTO param) {
        return orderGiftMapper.orderGiftList(param);
    }

    @Override
    public OrderGift selectFirstActivity(OrderGiftInfoAppDTO param) {
        return orderGiftMapper.selectFirstActivity(param);
    }

}
