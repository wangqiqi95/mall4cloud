package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.AdInfoDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.model.OpenScreenAd;
import com.mall4j.cloud.group.vo.OpenScreenAdListVO;

import java.util.List;

public interface OpenScreenAdMapper extends BaseMapper<OpenScreenAd> {
    List<OpenScreenAdListVO> openScreenAdList(OpenScreenAdPageDTO param);

    OpenScreenAd selectFirstActivity(AdInfoDTO param);
}
