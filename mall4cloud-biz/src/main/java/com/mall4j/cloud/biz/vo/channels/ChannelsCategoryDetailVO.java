package com.mall4j.cloud.biz.vo.channels;

import com.mall4j.cloud.api.biz.dto.channels.EcCat;
import com.mall4j.cloud.api.biz.dto.channels.EcCatAttr;
import com.mall4j.cloud.biz.model.channels.ChannelsCategory;
import lombok.Data;

@Data
public class ChannelsCategoryDetailVO {

    private ChannelsCategory channelsCategory;

    private EcCatAttr attr;

    private EcCat info;
}
