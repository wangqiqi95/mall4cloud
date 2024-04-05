package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.api.openapi.ipuhuo.vo.ShopInfoVO;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("shopInfoService")
@RefreshScope
public class ShopInfoServiceImpl implements IPuHuoProductHandleService, InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);
	private static final ReqMethodType reqMethodType = ReqMethodType.ShopInfo;

	@Value("${iph.shop.id:0}")
	private Long shopid;
	@Value("${iph.shop.name:暖多商城}")
	private String shopname;
	@Value("${iph.shop.logo:}")
	private String logo;

	@Override
	public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
		return IPuHuoRespDto.success(new ShopInfoVO(shopid, shopname, logo));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
	}
}
