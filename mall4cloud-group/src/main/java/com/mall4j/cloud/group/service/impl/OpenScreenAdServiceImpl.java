package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.dto.AdInfoDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.mapper.OpenScreenAdMapper;
import com.mall4j.cloud.group.model.OpenScreenAd;
import com.mall4j.cloud.group.service.OpenScreenAdService;
import com.mall4j.cloud.group.vo.OpenScreenAdListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OpenScreenAdServiceImpl extends ServiceImpl<OpenScreenAdMapper, OpenScreenAd> implements OpenScreenAdService {
    @Resource
    private OpenScreenAdMapper openScreenAdMapper;
    @Override
    public List<OpenScreenAdListVO> openScreenAdList(OpenScreenAdPageDTO param) {
        return openScreenAdMapper.openScreenAdList(param);
    }

    @Override
    public OpenScreenAd selectFirstActivity(AdInfoDTO param) {
        return openScreenAdMapper.selectFirstActivity(param);
    }
}
