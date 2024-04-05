package com.mall4j.cloud.openapi.service.impl.ipuhuo;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.openapi.ipuhuo.dto.*;
import com.mall4j.cloud.api.openapi.ipuhuo.enums.ReqMethodType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.IPuHuoProductHandleService;
import com.mall4j.cloud.openapi.service.impl.IPuHuoProductHandle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：上传图片业务操作层
 */
@Service("uploadImageService")
public class UploadImageServiceImpl implements IPuHuoProductHandleService, InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);
    private static final ReqMethodType reqMethodType = ReqMethodType.UploadImage;

    @Autowired
    MinioUploadFeignClient minioUploadFeignClient;

    // 固定门店id,无意义，只是返回给爱铺货使用
    private static final Integer shopId = 2048;

    @Override
    public IPuHuoRespDto<BaseResultDto> productAll(CommonReqDto commonReqDto, HttpServletRequest request) {
        long start = System.currentTimeMillis();
        UploadImageReqDto uploadImageReqDto = fillBeanWithRequest(request, new UploadImageReqDto(), true);
        IPuHuoRespDto<BaseResultDto> respDto = IPuHuoRespDto.fail("文件上传失败");
        ServerResponseEntity<String> responseEntity = null;
        String name = "";
        if (StringUtils.isNotBlank(commonReqDto.getBizcontent())) {
            name = JSON.parseObject(commonReqDto.getBizcontent()).getString("name");
        }
        if (StringUtils.isBlank(uploadImageReqDto.getName()) && StringUtils.isNotBlank(name)) {
            uploadImageReqDto.setName(name);
        }
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = new StandardMultipartHttpServletRequest(request);
            MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
            if (multipartFile == null) {
                throw new LuckException("上传的文件错误");
            }
            uploadImageReqDto.setFile(multipartFile);
            String originalFilename = multipartFile.getOriginalFilename();
            int pointIndex = -1;
            if (StringUtils.isNotBlank(originalFilename)) {
                pointIndex = originalFilename.lastIndexOf(".");
            }
            String ext = pointIndex != -1 ? originalFilename.substring(pointIndex) : "";
            String mimoPath = "/product/iph/" + (StringUtils.isBlank(ext) ? originalFilename : IdUtil.simpleUUID() + ext);
            responseEntity = minioUploadFeignClient.minioFileUpload(uploadImageReqDto.getFile(), mimoPath, uploadImageReqDto.getFile().getContentType());
            if (responseEntity != null && responseEntity.isSuccess()) {
                String url = responseEntity.getData();
                //替换地址为cdn
                url = url.replaceFirst("xcx-prd.oss-cn-shanghai.aliyuncs.com", "xcx-prd.skechers.cn");
                respDto = IPuHuoRespDto.success(new UploadImageRespDto(shopId, url));
            }
        } catch (Exception e) {
            logger.error("爱铺货推送-文件上传异常", e);
        } finally {
            logger.info("处理爱铺货上传图片结束，请求参数为：{}，feign请求结果为：{}, 处理结果为：{}，共耗时：{}", uploadImageReqDto, responseEntity, respDto, System.currentTimeMillis() - start);
        }
        return respDto;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IPuHuoProductHandle.getInstance().registHandler(reqMethodType, this);
    }
}
