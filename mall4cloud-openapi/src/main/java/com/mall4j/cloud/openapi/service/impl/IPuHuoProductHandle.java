package com.mall4j.cloud.openapi.service.impl;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IPuHuoProductHandle implements IPuHuoProductHandleService {
    private static final Logger logger = LoggerFactory.getLogger(IPuHuoProductHandle.class);

    private static final Map<ReqMethodType, IPuHuoProductHandleService> HANDLE_SERVICE_MAP = new ConcurrentHashMap<>();

    @Override
    public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
        // 2、找到对应的handle处理
        ReqMethodType reqMethodType = ReqMethodType.getReqMethodType(commonReqDto.getMethod());
        if (reqMethodType != null) {
            try {
                // 1、签名校验
                if (!commonReqDto.verifySign()) {
                    return IPuHuoRespDto.fail("签名不正确");
                }
                return HANDLE_SERVICE_MAP.get(reqMethodType).productAll(commonReqDto, request);
            } catch (Throwable e) {
                logger.error("", e);
                return IPuHuoRespDto.fail("请求失败，请联系管理员");
            }
        } else {
            return IPuHuoRespDto.fail("method不正确");
        }
    }

    public void registHandler(ReqMethodType reqMethodType, IPuHuoProductHandleService iPuHuoProductHandleService) {
        HANDLE_SERVICE_MAP.put(reqMethodType, iPuHuoProductHandleService);
    }

    private IPuHuoProductHandle() {
    }

    public static IPuHuoProductHandle getInstance() {
        return Instance.instance;
    }

    private static class Instance {
        private static IPuHuoProductHandle instance = new IPuHuoProductHandle();
    }
}
