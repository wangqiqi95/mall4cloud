package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.GoodListReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：获取商品列表业务操作层
 */
@Service("goodListService")
public class GoodListServiceImpl implements IPuHuoProductHandleService, InitializingBean {
    private static final ReqMethodType reqMethodType = ReqMethodType.GoodList;

    @Override
    public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
        GoodListReqDto goodListReqDto = JSONUtil.toBean(commonReqDto.getBizcontent(), new TypeReference<GoodListReqDto>() {
        }, true);
        // TODO 具体业务逻辑
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
    }
}
