package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.api.openapi.ipuhuo.vo.ShopInfoVO;
import com.mall4j.cloud.api.openapi.ipuhuo.vo.UserInfoVO;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("userInfoService")
@RefreshScope
public class UserInfoServiceImpl implements IPuHuoProductHandleService, InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);
	private static final ReqMethodType reqMethodType = ReqMethodType.UserInfo;

	@Value("${iph.user.id:0}")
	private Long userid;
	@Value("${iph.user.name:暖多}")
	private String username;
	@Value("${iph.user.nickname:}")
	private String nickname;

	@Override
	public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
		return IPuHuoRespDto.success(new UserInfoVO(userid, username, nickname));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
	}
}
