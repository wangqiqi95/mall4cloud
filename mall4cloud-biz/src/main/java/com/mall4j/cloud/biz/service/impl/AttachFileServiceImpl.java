package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.mall4j.cloud.biz.config.MinioTemplate;
import com.mall4j.cloud.biz.config.OssConfig;
import com.mall4j.cloud.biz.constant.OssType;
import com.mall4j.cloud.biz.dto.AttachFileDTO;
import com.mall4j.cloud.biz.mapper.AttachFileMapper;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.service.AttachFileGroupService;
import com.mall4j.cloud.biz.service.AttachFileService;
import com.mall4j.cloud.biz.vo.AttachFileGroupVO;
import com.mall4j.cloud.biz.vo.AttachFileVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 上传文件记录表
 *
 * @author YXF
 * @date 2020-11-21 10:21:40
 */
@Service
public class AttachFileServiceImpl implements AttachFileService {

    @Autowired
    private AttachFileMapper attachFileMapper;

    @Autowired
    private AttachFileGroupService attachFileGroupService;

    @Autowired
    private MinioTemplate minioTemplate;

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private MapperFacade mapperFacade;

    @Value("${biz.oss.resources-url}")
    private String imgDomain;

    @Override
    public PageVO<AttachFileVO> page(PageDTO pageDTO, AttachFileDTO attachFileDTO) {
        if (Objects.equals(AuthUserContext.get().getTenantId(), Constant.DEFAULT_SHOP_ID)) {
            // 如果店铺id还没有生成，使用uid查找文件信息
            attachFileDTO.setShopId(null);
            attachFileDTO.setUid(AuthUserContext.get().getUid());
        } else {
            attachFileDTO.setUid(null);
            attachFileDTO.setShopId(AuthUserContext.get().getTenantId());
        }
        return PageUtil.doPage(pageDTO, () -> attachFileMapper.list(attachFileDTO));
    }

    @Override
    public AttachFileVO getById(Long fileId) {
        return mapperFacade.map(attachFileMapper.getById(fileId), AttachFileVO.class);
    }

    @Override
    public void save(List<AttachFile> attachFiles) {
        attachFileMapper.save(attachFiles, AuthUserContext.get().getTenantId(), AuthUserContext.get().getUid());
    }

    @Override
    public void update(AttachFile attachFile) {
        attachFileMapper.update(attachFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long fileId) {
        AttachFile attachFile = attachFileMapper.getById(fileId);
        deleteFile(attachFile.getFilePath());
        attachFileMapper.deleteById(fileId);
    }

    @Override
    public Boolean updateFileName(AttachFile attachFile) {
        if (Objects.isNull(attachFile.getFileName()) && Objects.isNull(attachFile.getAttachFileGroupId())) {
            return Boolean.TRUE;
        }
        attachFileMapper.update(attachFile);
        return Boolean.TRUE;
    }

    @Override
    public void deleteByIdsAndShopId(List<Long> ids, Long shopId) {
        List<AttachFileVO> attachFileList = attachFileMapper.getByIds(ids);
        // 获取文件的实际路径--数据库中保存的文件路径为： / + 实际的文件路径
        List<String> filePaths = attachFileList.stream().map(item -> {
            if (!Objects.equals(item.getShopId(), shopId)) {
                throw new LuckException("存在非本店铺下的文件，删除失败");
            }
            return item.getFilePath().substring(1);
        }).collect(Collectors.toList());
        this.batchDeleteFiles(filePaths);
        attachFileMapper.batchDeleteByIds(ids);
    }

    @Override
    public void batchMoveByShopIdAndIdsAndGroupId(Long shopId, List<Long> ids, Long groupId) {
        if (Objects.nonNull(groupId)) {
            AttachFileGroupVO attachFileGroupVO = attachFileGroupService.getByAttachFileGroupId(groupId);
            if (Objects.isNull(attachFileGroupVO)) {
                throw new LuckException("当前分组已不存在，请刷新后重试");
            }
            if (!Objects.equals(attachFileGroupVO.getShopId(), shopId)) {
                throw new LuckException("分组信息错误");
            }
        }
        attachFileMapper.batchMoveByShopIdAndIdsAndGroupId(shopId, ids, groupId);
    }

    @Override
    public void updateShopIdByUid(Long shopId, Long uid) {
        attachFileMapper.updateShopIdByUid(shopId, uid);
    }

    @Override
    public void saveFile(AttachFile attachFile) {
        attachFileMapper.saveFile(attachFile, AuthUserContext.get().getUid());
    }

    /**
     * 根据文件路径，删除文件
     *
     * @param filePath 文件路径
     */
    public void deleteFile(String filePath) {
        if (Objects.equals(ossConfig.getOssType(), OssType.ALI.value())) {
            // 获取文件的实际路径--数据库中保存的文件路径为： / + 实际的文件路径
            if (StrUtil.isNotBlank(filePath)) {
                filePath = filePath.substring(1);
            }
            String endpoint = ossConfig.getEndpoint();
            String bucket = ossConfig.getBucket();
            String keyId = ossConfig.getAccessKeyId();
            String keySecret = ossConfig.getAccessKeySecret();
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);
            try {
                ossClient.deleteObject(bucket, filePath);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            } finally {
                ossClient.shutdown();
            }
        } else if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
            // TODO 待测试
            minioTemplate.removeObject(filePath);
        } else if (Objects.equals(ossConfig.getOssType(), OssType.Q_CLOUD.value())) {
            //腾讯云
            COSClient cosClient = cosClient(ossConfig);
            try {
                cosClient.deleteObject(ossConfig.getBucket(),filePath);
            } catch (Exception e){
                e.printStackTrace();
                System.err.println(e.getMessage());
            } finally {
                cosClient.shutdown();
            }
        }
    }

    /**
     * 根据文件路径批量删除文件
     *
     * @param filePaths 文件路径列表
     */
    private void batchDeleteFiles(List<String> filePaths) {
        if (Objects.equals(ossConfig.getOssType(), OssType.ALI.value())) {
            String endpoint = ossConfig.getEndpoint();
            String bucket = ossConfig.getBucket();
            String keyId = ossConfig.getAccessKeyId();
            String keySecret = ossConfig.getAccessKeySecret();
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);
            try {
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(filePaths));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            } finally {
                ossClient.shutdown();
            }
        } else if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
            // TODO 待测试
            minioTemplate.removeObjects(filePaths);
        } else if (Objects.equals(ossConfig.getOssType(), OssType.Q_CLOUD.value())) {
            //腾讯云
            COSClient cosClient = cosClient(ossConfig);
            com.qcloud.cos.model.DeleteObjectsRequest deleteObjectsRequest = new com.qcloud.cos.model.DeleteObjectsRequest(ossConfig.getBucket());
            ArrayList<com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
            for (String filePath : filePaths) {
                keyList.add(new com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion(filePath));
            }
            deleteObjectsRequest.setKeys(keyList);
            try {
                cosClient.deleteObjects(deleteObjectsRequest);
            } catch (Exception e){
                e.printStackTrace();
                System.err.println(e.getMessage());
            } finally {
                cosClient.shutdown();
            }
        }
    }

    private COSClient cosClient(OssConfig ossConfig) {
        BasicCOSCredentials cred = new BasicCOSCredentials(ossConfig.getAccessId(), ossConfig.getAccessKeySecret());
        Region region = new Region(ossConfig.getEndpoint());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }
}
