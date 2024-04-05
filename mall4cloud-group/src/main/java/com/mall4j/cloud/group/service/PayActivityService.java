package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.dto.PayActivityPageDTO;
import com.mall4j.cloud.group.model.PayActivity;
import com.mall4j.cloud.group.vo.PayActivityListVO;

import java.util.List;

public interface PayActivityService extends IService<PayActivity> {
    List<PayActivityListVO> payActivityList(PayActivityPageDTO param);

    PayActivity selectFirstActivity(String shopId);
}
