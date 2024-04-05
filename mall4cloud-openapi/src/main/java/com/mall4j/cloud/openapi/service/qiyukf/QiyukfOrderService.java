package com.mall4j.cloud.openapi.service.qiyukf;

import com.mall4j.cloud.api.openapi.dto.qiyukf.QiyukfQueryDTO;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfOrderServerResponseEntity;
import com.mall4j.cloud.api.openapi.vo.qiyukf.QiyukfTokenVO;

import javax.servlet.http.HttpServletRequest;

public interface QiyukfOrderService {

    QiyukfTokenVO getToken(String appid, String appsecret);

    QiyukfOrderServerResponseEntity getTheOrderPageByUser(QiyukfQueryDTO qiyukfQueryDTO, HttpServletRequest request);


}
