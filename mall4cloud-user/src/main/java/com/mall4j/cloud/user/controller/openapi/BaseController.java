package com.mall4j.cloud.user.controller.openapi;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.user.config.OpenApiParams;
import com.mall4j.cloud.user.vo.openapi.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private OpenApiParams openApiParams;

	protected ApiResponse<Void> verify(String jsonRequest) {
		String sign = request.getHeader("sign");
		String timestamp = request.getHeader("timestamp");
		String secretKey = request.getHeader("secretKey");
		log.info(StrUtil.format("接口鉴权sign =:{},timestamp：{}，secretKey：{}",sign,timestamp,secretKey));
		if (StringUtils.isBlank(secretKey)) {
			return ApiResponse.fail("Header参数：secretKey不能为空");
		}
		if (!secretKey.equals(openApiParams.getSecretKey())) {
			return ApiResponse.fail("Header参数：secret不正确");
		}
		if (StringUtils.isBlank(timestamp)) {
			return ApiResponse.fail("Header参数：timestamp不能为空");
		}
		if (StringUtils.isBlank(sign)) {
			return ApiResponse.fail("Header参数：sign不能为空");
		}
		if (!sign.equals(this.createSign(timestamp,jsonRequest))) {
			return ApiResponse.fail("Header参数：sign不正确");
		}
		return ApiResponse.success();
	}

	/**
	 * 生成签名算法
	 *
	 * @param timestamp 当前时间的时间戳（毫秒）
	 * @return 加密签名
	 * @throwsUnsupportedEncodingException
	 */
	public String createSign(String timestamp,String jsonRequest) {
		try {
			String str = timestamp + "&" + openApiParams.getSecretKey()+"&"+jsonRequest;
			String urlEncode = java.net.URLEncoder.encode(str.toLowerCase(), "utf-8").toLowerCase();
			String strMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(urlEncode);
			log.info(StrUtil.format("生成sign参数:{},生成sign值为：{}",str,strMd5));
			return strMd5.toLowerCase();
		} catch (UnsupportedEncodingException e) {
			logger.error("生成签名异常，timestamp=" + timestamp, e);
		}
		return "";
	}

	public static void main(String[] args) {
//		long timestamp = System.currentTimeMillis();
		long timestamp = 1710136200188L;
		System.out.println(timestamp);

		try {
			String str = timestamp + "&" + "gRQJ2CMqP18h60hdnbcRCYsd"+"&"+"{\"sendTime\":1710136200184,\"taskId\":24,\"userIds\":[\"wmmXETLgAASOC-Egc7s_jkzFKD-y9iYw\",\"wmmXETLgAA6hNwOgiohvQnzqQK-53DnA\",\"wmmXETLgAAFzAlLV-17qldJBKR2GXV-g\",\"wmmXETLgAAuJttIsyJG3tu6Gfw2FsbLw\",\"wmmXETLgAARHtzKKxNAX6Wrj6ZEYRG1g\",\"wmmXETLgAATli_ILQ_Y_rWL-Nyo36Jcg\",\"wmmXETLgAABmvqwRolty-X9_835lOlow\",\"wmmXETLgAAekyUKNMk3H3W8H2oRh4ccQ\",\"wmmXETLgAAVgHWvupHxhCwj1_0iI_ogw\",\"wmmXETLgAAbNclM-s7AWYf0QiQES3qcQ\",\"wmmXETLgAAp1kxZjehjnS9PLEMd21kyA\",\"wmmXETLgAA0LsCMfbjGjuq0QtMuGYfSQ\",\"wmmXETLgAAtbqt27g0H86EXDMTZ-gG9g\",\"wmmXETLgAA36iZtAE7kMLHQWWO2oUBag\",\"wmmXETLgAAYQRar7bVf4jkJqAZ_pruEQ\",\"wmmXETLgAAFju7RMf-UAj_ka5dcWbdWA\",\"wmmXETLgAAE2LiCD2a3P4Y4zdnShaPVA\",\"wmmXETLgAAQxBmh9CYqXCwNO9XIM6e-g\",\"wmmXETLgAA7yhDPN8AIfFhQptpXeJNGg\",\"wmmXETLgAAP1ptpYGy6CQYAv9fNXQ8hQ\",\"wmmXETLgAAp-LzqfVjXWm9AidkdFVZUw\",\"wmmXETLgAAHPhpWEEFswUlsJ5y7m9-RQ\",\"wmmXETLgAAVO5AVTFyr3pcwuZW7lhSWw\",\"wmmXETLgAA8j_6OAJD2YNHASfUw9SGJg\",\"wmmXETLgAAhlZlLvnPEqLsPK3CYODxBw\",\"wmmXETLgAAswoJSBG3cfhhxTbxCTKTJQ\",\"wmmXETLgAAMsNXbSRs01X45ARJQWryAA\",\"wmmXETLgAA08ELamMLsEkV9dWOTHOhcw\",\"wmmXETLgAAPBZe0OrfsjDOm0M0Y21Agg\",\"wmmXETLgAAzj-AqMawE438OThMrlfRcg\",\"wmmXETLgAAFCyGybRJrbIA4P_T7CTJjw\",\"wmmXETLgAA7ILuKlBRyAEoEf-VWrJ-ug\",\"wmmXETLgAAadYvgwfGrD7HL1ig0ZxRwA\",\"wmmXETLgAAx6VdSTaOT3ki6sQkWeSBcg\",\"wmmXETLgAAKMQWEOzDrLQNyc_l1Epy0w\",\"wmmXETLgAAc8csmQp-LfeMpcHKIi_8DQ\",\"wmmXETLgAAlgibZUMBA4ZKy7VPBCQMQQ\",\"wmmXETLgAAu7p55kF8l1WjME4PuhMQpQ\",\"wmmXETLgAAYRm7_ux5aVjgZQdpiXuIEw\",\"wmmXETLgAAoz7-IahNJTS_Ob2j9P3QOA\",\"wmmXETLgAAvRuGOyZ5W7K841_7pxIUnw\",\"womXETLgAAHqRzszSxv00s-eQKoTjJBw\",\"wmmXETLgAAJHeujnCScx1gTmRTUZsqXw\",\"wmmXETLgAAYNVXw-VAFvFYH3gFWz_R8Q\",\"wmmXETLgAAG9y5nMj3gBwvPsN-9WgmlQ\",\"wmmXETLgAA_ckTD4j-tgk7Blgoh6txmw\",\"wmmXETLgAAgtgqlUDXyKQMhjyHbfEc3g\",\"wmmXETLgAA6gzqhCtUpqIxUNfJyfjA0Q\",\"wmmXETLgAAsxRyUSKJ8KXqZG2gx95utQ\",\"wmmXETLgAAKrI9TGxubzn6YeuoCg2T7Q\",\"wmmXETLgAAxiLPnEU1shVmFRINeuLgLg\",\"wmmXETLgAAblzmY9rusYGOBJwR520WPg\",\"wmmXETLgAARHZRfclgpBW_rJIc1QvygA\",\"wmmXETLgAA2dBNlVI6Ynmsu26FDbTY2w\",\"wmmXETLgAAU_2fBrWWj5qpWSAM-3qNgQ\",\"wmmXETLgAAS3w5EsAxHDbbA-GsQbzAgg\",\"wmmXETLgAArtU13PlSMaPMcWzsfUK2rg\",\"wmmXETLgAA7Cy6NPumsXhTANbTjqiQGw\",\"wmmXETLgAA7E07FcfLGCnxZqGds22BXA\",\"wmmXETLgAAQgeYwvcYrhgh_qXyU_TekQ\",\"wmmXETLgAAMrPxDlpcyfIOFDyHXZ5XWQ\",\"wmmXETLgAAhB0Tkx-j8gBXq3Vnh2maEA\",\"wmmXETLgAAhh6WabzQ9Yb7LoFBRddNVA\",\"wmmXETLgAAlFaFEBeGQ_VSDL6R0Vzmrw\",\"wmmXETLgAAy-R6_mpHBA2UVhir--5-BA\",\"wmmXETLgAAdtmQmARex5ZXW-p4vkL8ng\",\"wmmXETLgAAqrLyb1EO6WKgWlsY5THAFw\",\"wmmXETLgAAeL-HhjXuU0swLuvQ2h28BQ\"]}";
			String urlEncode = java.net.URLEncoder.encode(str.toLowerCase(), "utf-8").toLowerCase();
			String strMd5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(urlEncode);
			System.out.println(strMd5);

		} catch (UnsupportedEncodingException e) {
		}

	}
}
