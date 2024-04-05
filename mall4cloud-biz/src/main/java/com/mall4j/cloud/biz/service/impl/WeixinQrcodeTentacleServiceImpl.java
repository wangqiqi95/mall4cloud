package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.biz.constant.SceneType;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleDTO;
import com.mall4j.cloud.biz.event.TentacleQrcodeEvent;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleStoreService;
import com.mall4j.cloud.biz.util.CompressUtil;
import com.mall4j.cloud.biz.util.SendEmail;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleVO;
import com.mall4j.cloud.biz.vo.WxQrCodeFileVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.mapper.WeixinQrcodeTentacleMapper;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信触点二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
@Slf4j
@Service
public class WeixinQrcodeTentacleServiceImpl implements WeixinQrcodeTentacleService {

    @Autowired
    private WeixinQrcodeTentacleMapper weixinQrcodeTentacleMapper;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private WeixinQrcodeTentacleStoreService tentacleStoreService;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SendEmail sendEmail;
    @Value(value = "${spring.mail.subject}")
    private String subject; //发件者的邮箱
    @Value(value = "${spring.mail.content}")
    private String content; //发件者的邮箱

    @Override
    public PageVO<WeixinQrcodeTentacleVO> page(PageDTO pageDTO, String name, String storeId) {
        return PageUtil.doPage(pageDTO, () -> weixinQrcodeTentacleMapper.getList(name,storeId));
    }

    @Override
    public WeixinQrcodeTentacle getById(String id) {
        return weixinQrcodeTentacleMapper.getById(id);
    }

    @Override
    public void save(WeixinQrcodeTentacleDTO weixinQrcodeTentacleDTO) {
//        StringBuffer tentacleSign = new StringBuffer();
        WeixinQrcodeTentacle weixinQrcodeTentacle = mapperFacade.map(weixinQrcodeTentacleDTO, WeixinQrcodeTentacle.class);
        weixinQrcodeTentacle.setId(RandomUtil.getUniqueNumStr());
        weixinQrcodeTentacle.setStatus(0);
        weixinQrcodeTentacle.setDelFlag(0);
        weixinQrcodeTentacle.setCreateTime(new Date());
//        tentacleSign.append(weixinQrcodeTentacle.getTentaclePath());
//        if(StringUtils.isNotEmpty(weixinQrcodeTentacle.getTentaclePath())){
//            if (weixinQrcodeTentacle.getTentaclePath().indexOf("?") != -1) {
//                tentacleSign.append("l=");
//                tentacleSign.append(weixinQrcodeTentacle.getId());
//                tentacleSign.append("&sc=");
//                tentacleSign.append( SceneType.TENTACLE.getValue());
//            } else {
//                tentacleSign.append("?l=");
//                tentacleSign.append(weixinQrcodeTentacle.getId());
//                tentacleSign.append("&sc=");
//                tentacleSign.append( SceneType.TENTACLE.getValue());
//            }
//            weixinQrcodeTentacle.setTentaclePath(tentacleSign.toString());
//            log.info("触点新增拼接后url：{}", weixinQrcodeTentacle.getTentaclePath());
//        }
        weixinQrcodeTentacleMapper.save(weixinQrcodeTentacle);

        //保存门店
        if(StrUtil.isNotBlank(weixinQrcodeTentacleDTO.getStoreIds())){

            TentacleQrcodeEvent soldSpuEvent=new TentacleQrcodeEvent();
            soldSpuEvent.setTentacleDTO(weixinQrcodeTentacleDTO);
            soldSpuEvent.setWeixinQrcodeTentacle(weixinQrcodeTentacle);
            applicationContext.publishEvent(soldSpuEvent);

//            List<WxQrCodeFileVO> codeFileVOS=new ArrayList<>();
//            //保存门店及生成二维码信息
//            tentacleStoreService.saveTentacleStore(codeFileVOS,weixinQrcodeTentacle,weixinQrcodeTentacleDTO.getStoreIds());
//
//            if(CollectionUtil.isNotEmpty(codeFileVOS)){
//                try {
//                    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                    String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";
//
//                    File copyFile=new File(zipFilePathExport);
//                    copyFile.mkdirs();
//
//                    List<File> fileList=new ArrayList<>();
//                    //文件存放统一目录
//                    copyCodeToFile(codeFileVOS,zipFilePathExport,fileList);
//
//                    log.info("保存微信触点二维码 fileList【{}】",fileList.size());
//                    if(CollectionUtil.isNotEmpty(fileList)){
//                        //压缩统一文件目录
//                        String zipPath= CompressUtil.zipFile(copyFile,"zip");
//                        log.info("保存微信触点二维码 zip zipPath【{}】",zipPath);
//                        if(new File(zipPath).isFile()){
//                            File zipFile=new File(zipPath);
//                            FileInputStream fileInputStream = new FileInputStream(zipFile);
//                            MultipartFile multipartFile = new MultipartFileDto(zipFile.getName(), zipFile.getName(),
//                                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
//
//                            String originalFilename = multipartFile.getOriginalFilename();
//                            int pointIndex = -1;
//                            if (StringUtils.isNotBlank(originalFilename)) {
//                                pointIndex = originalFilename.lastIndexOf(".");
//                            }
//                            String mimoPath = "wxqrcode/tentacle/" + originalFilename;
//                            ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
//                            if(responseEntity.isSuccess()){
//                                log.info("保存微信触点二维码 zip url 【{}】",responseEntity.getData());
//                                weixinQrcodeTentacle.setCodeZipPath(responseEntity.getData());
//                                this.update(weixinQrcodeTentacle);
//                            }
//                            //删除本地文件
//                            File file=new File(zipPath);
//                            FileUtil.clean(file);
//                            FileUtil.clean(copyFile);
//                            file.delete();
//                            copyFile.delete();
//                        }
//                    }
//
//                }catch (Exception e){
//                    log.info("保存微信触点二维码1 操作失败 【{}】 【{}】",e,e.getMessage());
//                    throw new LuckException("操作失败");
//                }
//            }
        }
    }

    /**
     * 复制本地生成二维码到统一目录，打包zip
     * @param fileList 文件
     * @param zipFilePathExport 统一存放目录
     * @param fileList 新的文件
     */
    private void copyCodeToFile(List<WxQrCodeFileVO> codeFileVOS,String zipFilePathExport,List<File> fileList){
        try {
            for(WxQrCodeFileVO codeFileVO:codeFileVOS){
                String originalFilename=codeFileVO.getFile().getName();
                int pointIndex = -1;
                if (StringUtils.isNotBlank(originalFilename)) {
                    pointIndex = originalFilename.lastIndexOf(".");
                }
                String ext = pointIndex != -1 ? originalFilename.substring(pointIndex) : "";
                String saveFileName=zipFilePathExport+""+codeFileVO.getStoreId()+"_"+codeFileVO.getStoreName()+"_"+System.currentTimeMillis()+ext;
                File outFile=new File(saveFileName);
                FileCopyUtils.copy(codeFileVO.getFile(),outFile);
                fileList.add(outFile);
                //删除本地文件
                FileUtil.clean(codeFileVO.getFile());
                codeFileVO.getFile().delete();
            }
        }catch (Exception e){
            log.info(""+e.getMessage());
        }
    }

    @Override
    public void update(WeixinQrcodeTentacle weixinQrcodeTentacle) {
        weixinQrcodeTentacleMapper.update(weixinQrcodeTentacle);
    }

    @Override
    public void deleteById(String id) {
        weixinQrcodeTentacleMapper.deleteById(id);
    }

    @Override
    public ServerResponseEntity<String> sendQrcodeZipToEmail(String id,String receiveEmail) {
        WeixinQrcodeTentacle weixinQrcodeTentacle=this.getById(id);
        if(weixinQrcodeTentacle==null){
            throw new LuckException("邮件发送失败，未找到对应数据");
        }
        if(StringUtils.isEmpty(receiveEmail)){
            throw new LuckException("邮件发送失败，接收邮箱为空");
        }
        if(StringUtils.isEmpty(weixinQrcodeTentacle.getCodeZipPath())){
            throw new LuckException("邮件发送失败，触点码文件为空");
        }
        try {
            String toUser=receiveEmail;
            String filePath=weixinQrcodeTentacle.getCodeZipPath();
            String fileName=filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
            sendEmail.sendRemoteFileMail(toUser,subject,content,filePath,fileName);
            return ServerResponseEntity.success("邮件已发送");
        }catch (Exception e){
            log.info(""+e.getMessage());
            return ServerResponseEntity.showFailMsg("邮件发送失败");
        }
    }

}
