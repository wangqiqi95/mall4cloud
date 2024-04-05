package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GoodsReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：获取商品详细信息业务操作层
 */
@Service("goodsService")
public class GoodsServiceImpl implements IPuHuoProductHandleService, InitializingBean {
    private static final ReqMethodType reqMethodType = ReqMethodType.Goods;

    @Override
    public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
        GoodsReqDto goodsReqDto = JSONUtil.toBean(commonReqDto.getBizcontent(), new TypeReference<GoodsReqDto>() {
        }, true);
        // TODO 具体业务逻辑
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
    }
}
