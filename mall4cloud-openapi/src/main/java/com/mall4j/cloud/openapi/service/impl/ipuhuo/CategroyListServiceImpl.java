package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CategoryReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.CommonReqDto;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.IPuHuoRespDto;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.api.openapi.ipuhuo.vo.CategoryVO;
import com.mall4j.cloud.api.product.feign.CategoryFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service("categroyListService")
@RefreshScope
public class CategroyListServiceImpl implements IPuHuoProductHandleService, InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);
	private static final ReqMethodType reqMethodType = ReqMethodType.CategoryList;

	@Autowired CategoryFeignClient categoryFeignClient;

	@Override
	public IPuHuoRespDto productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		CategoryReqDto categoryReqDto = JSONUtil.toBean(commonReqDto.getBizcontent(), new TypeReference<CategoryReqDto>() {
		}, true);
		IPuHuoRespDto<List<CategoryVO>> respDto = IPuHuoRespDto.fail("获取类目失败");
		ServerResponseEntity<List<com.mall4j.cloud.common.product.vo.CategoryVO>> responseEntity = ServerResponseEntity.success();
		try {
			if (StringUtils.isBlank(categoryReqDto.getParentcid())) {
				categoryReqDto.setParentcid("0");
			}
			String parentcid = categoryReqDto.getParentcid();
			long l = 0l;
			try {
				l = Long.parseLong(parentcid);
			} catch (NumberFormatException e) {
				return IPuHuoRespDto.fail("parentcid格式不正确");
			}

			responseEntity = categoryFeignClient.listByParentId(l, 1l);
			if (responseEntity != null && responseEntity.isSuccess()) {
				List<com.mall4j.cloud.common.product.vo.CategoryVO> datas = responseEntity.getData();
				List<CategoryVO> categoryVOS = new ArrayList<>();
				datas.stream().forEach(temp -> {
					CategoryVO categoryVO = new CategoryVO();
					categoryVO.setCid(temp.getCategoryId());
					categoryVO.setLevel(temp.getLevel());
					categoryVO.setName(temp.getName());
					categoryVO.setParentid(temp.getParentId());
					categoryVO.setIsleaf((temp.getLevel() != null && temp.getLevel() == 2) ? 1 : 0);
					categoryVOS.add(categoryVO);
				});
				respDto = IPuHuoRespDto.success(categoryVOS);
			}
		} catch (Exception e) {
			logger.error("爱铺货获取商品类目异常", e);
		} finally {
			logger.info("处理爱铺货获取商品类目结束，请求参数为：{}，feign请求结果为：{}, 处理结果为：{}，共耗时：{}", categoryReqDto, responseEntity, respDto, System.currentTimeMillis() - start);
		}
		return respDto;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
	}
}
