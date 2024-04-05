package com.mall4j.cloud.auth.controller;

import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2020/7/30
 */
@RestController("uaCaptchaController")
@RequestMapping("/ua/captcha")
@Api(tags = "验证码")
public class CaptchaController {

	private final CaptchaService captchaService;

	public CaptchaController(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	@PostMapping({ "/get" })
	public ServerResponseEntity<ResponseModel> get(@RequestBody CaptchaVO captchaVO) {
		return ServerResponseEntity.success(captchaService.get(captchaVO));
	}

	@PostMapping({ "/check" })
	public ServerResponseEntity<ResponseModel> check(@RequestBody CaptchaVO captchaVO) {
		ResponseModel responseModel;
		try {
			responseModel = captchaService.check(captchaVO);
		}catch (Exception e) {
			return ServerResponseEntity.success(ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR));
		}
		return ServerResponseEntity.success(responseModel);
	}

}
