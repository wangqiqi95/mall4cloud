package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.dto.AdInfoDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.model.OpenScreenAd;
import com.mall4j.cloud.group.vo.OpenScreenAdListVO;

import java.util.List;

public interface OpenScreenAdService extends IService<OpenScreenAd> {
    List<OpenScreenAdListVO> openScreenAdList(OpenScreenAdPageDTO param);

    OpenScreenAd selectFirstActivity(AdInfoDTO param);
}
