package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.AddGoodsTagReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.AddGoodsTagRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.api.product.dto.IphSyncCategoryDto;
import com.mall4j.cloud.api.product.feign.CategoryFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：创建分组，爱铺货推送
 *
 * @date 2022/2/24 15:59：16
 */
@Service("addGoodsTagService")
public class AddGoodsTagServiceImpl implements IPuHuoProductHandleService, InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);
	private static final ReqMethodType reqMethodType = ReqMethodType.AddGoodsTag;

	@Autowired
	private CategoryFeignClient categoryFeignClient;

	@Override
	public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		AddGoodsTagReqDto addGoodsTagReqDto = JSONUtil.toBean(commonReqDto.getBizcontent(), new TypeReference<AddGoodsTagReqDto>() {
		}, true);
		IPuHuoRespDto<BaseResultDto> respDto = IPuHuoRespDto.fail("请求失败，请联系管理员");
		ServerResponseEntity<Long> longServerResponseEntity = null;
		try {
			if (addGoodsTagReqDto == null) {
				return IPuHuoRespDto.fail("请求参数为空");
			}
			if (addGoodsTagReqDto.getParentid() == null) {
				return IPuHuoRespDto.fail("商品父级分组id为空");
			}
			if (StringUtils.isBlank(addGoodsTagReqDto.getName())) {
				return IPuHuoRespDto.fail("商品分组的名称为空");
			}

			longServerResponseEntity = categoryFeignClient
					.syncCategoryByIPH(new IphSyncCategoryDto(addGoodsTagReqDto.getParentid(),addGoodsTagReqDto.getName()));
			if (longServerResponseEntity.isSuccess()) {
				respDto = IPuHuoRespDto.success(new AddGoodsTagRespDto(longServerResponseEntity.getData()));
			} else {
				respDto = IPuHuoRespDto.fail(longServerResponseEntity.getMsg());
			}
		} finally {
			logger.info("处理爱铺货添加商品分组推送信息结束，请求参数为：{}，feign请求结果为：{}, 处理结果为：{}，共耗时：{}", addGoodsTagReqDto, longServerResponseEntity, respDto, System.currentTimeMillis() - start);
		}
		return respDto;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
	}
}
