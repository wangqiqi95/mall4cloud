package com.mall4j.cloud.biz.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import com.mall4j.cloud.biz.config.*;
import com.mall4j.cloud.biz.constant.OssType;
import com.mall4j.cloud.biz.vo.OssVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/9/10
 */
@RequestMapping(value = "/ua/oss")
@RestController
@Api(tags = "文件管理")
public class OssController {

    private static final Logger log = LoggerFactory.getLogger(OssController.class);

    /**
     * 上传的文件夹(根据时间确定)
     */
    public static final String NORM_DAY_PATTERN = "yyyy/MM/dd";

    public static final String AGREE_ON = "://";

    /**
     * 上传文件要求的最小长度
     */
    public static final Integer MIN_LENGTH = 0;

    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private OSS ossClient;
    @Autowired
    private MinioTemplate minioTemplate;

    @PostMapping(value = "/uploadAwsS3")
    @ApiOperation(value = "uploadAwsS3", notes = "uploadAwsS3")
    public ServerResponseEntity<String> uploadAwsS3(@RequestPart("excelFile") MultipartFile file) {
        try {
            String url="";
            //亚马逊S3文件上传
//            url=awsS3Template.uploadPublic(file);
//            url=awsS3Template.upload(file);
//            if(StrUtil.isNotEmpty(url)){
//                url=url.replace(domainConfig.getAwsS3Domain()+"","");
//            }
            return ServerResponseEntity.success(url);
        } catch (Exception e) {
            log.error(  "文件上传异常 {}", e);
            throw new LuckException("文件上传失败");
        } finally {
            log.info("文件上传请求结束");
        }
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "token", notes = "获取文件上传需要的token")
    @ApiImplicitParam(name = "fileNum", value = "需要获取token的文件数量", defaultValue = "0")
    public ServerResponseEntity<OssVO> info(@RequestParam("fileNum") Integer fileNum,
                                            @RequestParam(required = false, value = "fileType") String fileType) {
        OssVO ossVO = new OssVO();
        // 阿里文件上传
        if (Objects.equals(ossConfig.getOssType(), OssType.ALI.value())) {
            fillAliOssInfo(ossVO, fileNum, fileType);
        }
        // minio文件上传
        else if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
            fillMinIoInfo(ossVO, fileNum, fileType);
        }
        //腾讯云cos文件上传
        else if (Objects.equals(ossConfig.getOssType(), OssType.Q_CLOUD.value())) {
            fillQcloudInfo(ossVO, fileNum, fileType);
        }
        //亚马逊s3
        else if (Objects.equals(ossConfig.getOssType(), OssType.AMAZON_S3.value())) {
//            fillAwsS3QcloudInfo(ossVO, fileNum, fileType);
        }
        return ServerResponseEntity.success(ossVO);
    }

    @GetMapping("image_resize")
    @ApiOperation(value = "oss图片调整大小", notes = "oss图片调整大小")
    @ApiImplicitParams({@ApiImplicitParam(name = "imageUrl",value = "oss图片路径",required = true),
                        @ApiImplicitParam(name = "width",value = "宽度px(范围1-16384)",defaultValue = "450"),
                        @ApiImplicitParam(name = "height",value = "高度px(范围1-16384)",defaultValue = "450")})
    public ServerResponseEntity<String> imageResize(@RequestParam String imageUrl,
                                                    @RequestParam(defaultValue = "450") int width,
                                                    @RequestParam(defaultValue = "450") int height) {
        String str="";
        if(imageUrl.matches(".+\\?")){
            str="&x-oss-process=image/resize,m_pad,h_" + height + ",w_" + width;
        }else{
            str="?x-oss-process=image/resize,m_pad,h_" + height + ",w_" + width;
        }
        return ServerResponseEntity.success(imageUrl+str);
    }

    private void fillAliOssInfo(OssVO ossVO, Integer fileNum, String fileType) {
        loadOssVO(ossVO, fileType);
        String[] split = ossConfig.getEndpoint().split(AGREE_ON);
        // host的格式为 bucketname.endpoint
        String host = split[0] + AGREE_ON + ossConfig.getBucket() + StrUtil.DOT + split[1];
        try {
            long expireTime = 30 * 1000L;
            long expireEndTime = System.currentTimeMillis() + expireTime;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            // 20
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, MIN_LENGTH, ossConfig.getMaxLength() * 1024 * 1024L);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, ossVO.getDir());

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            ossVO.setAccessid(ossConfig.getAccessId());
            ossVO.setHost(host);
            ossVO.setSignature(postSignature);
            ossVO.setPolicy(encodedPolicy);
            ossVO.setExpire(30);
            // 文件名称
            List<OssVO> ossVOList = new ArrayList<>();
            for (int i = 0; i < fileNum; i++) {
                OssVO oss = loadOssVO(new OssVO(), fileType);
                ossVOList.add(oss);
            }
            ossVO.setOssList(ossVOList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void fillMinIoInfo(OssVO ossVo, Integer fileNum, String fileType) {
        List<OssVO> ossVOList = new ArrayList<>();
        for (int i = 0; i < fileNum; i++) {
            OssVO oss = loadOssVO(new OssVO(), fileType);
            String actionUrl = minioTemplate.getPresignedObjectUrl(oss.getDir() + oss.getFileName());
            oss.setActionUrl(actionUrl);
            ossVOList.add(oss);
        }
        ossVo.setOssList(ossVOList);
    }

    private void fillQcloudInfo(OssVO ossVo, Integer fileNum, String fileType) {
        ArrayList<OssVO> ossVOList = new ArrayList<>();
        for (int i = 0; i < fileNum; i++) {
            OssVO oss = loadOssVO(new OssVO(), fileType);
            BasicCOSCredentials cred = new BasicCOSCredentials(ossConfig.getAccessId(), ossConfig.getAccessKeySecret());
            Region region = new Region(ossConfig.getEndpoint());
            ClientConfig clientConfig = new ClientConfig(region);
            clientConfig.setHttpProtocol(HttpProtocol.https);
            COSClient cosClient = new COSClient(cred, clientConfig);
            Date expirationTime = new Date(System.currentTimeMillis() + 30 * 1000L);
            URL url = cosClient.generatePresignedUrl(ossConfig.getBucket(), oss.getDir() + oss.getFileName(), expirationTime, HttpMethodName.PUT);
            oss.setActionUrl(url.toString());
            ossVOList.add(oss);
            cosClient.shutdown();
        }
        ossVo.setOssList(ossVOList);
    }

//    private void fillAwsS3QcloudInfo(OssVO ossVo, Integer fileNum, String fileType) {
//        ossVo.setHost(awsS3Config.getEndpoint());
//        loadOssVO(ossVo, fileType);
//        ArrayList<OssVO> ossVOList = new ArrayList<>();
//        for (int i = 0; i < fileNum; i++) {
//            OssVO oss = loadOssVO(new OssVO(), fileType);
//            ossVOList.add(oss);
//        }
//        ossVo.setOssList(ossVOList);
//    }


    private OssVO loadOssVO(OssVO ossVo, String fileType) {
        String dir = DateUtil.format(new Date(), NORM_DAY_PATTERN) + "/";
        String fileName = IdUtil.simpleUUID();
        if (StrUtil.isNotBlank(fileType)) {
            fileName += "." + fileType;
        }
        ossVo.setDir(dir);
        ossVo.setFileName(fileName);
        return ossVo;
    }

}
