package com.mall4j.cloud.biz.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncoder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.wx.wx.util.MaSunCodeUtils;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.QrcodeTentacleStoreExport;
import com.mall4j.cloud.biz.dto.StoreCodeItemExport;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreExportDTO;
import com.mall4j.cloud.biz.dto.WeixinQrcodeTentacleStoreItemDTO;
import com.mall4j.cloud.biz.mapper.WeixinQrcodeTentacleStoreItemMapper;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacle;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacleStoreItem;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleService;
import com.mall4j.cloud.biz.util.CompressUtil;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemExportVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreVO;
import com.mall4j.cloud.biz.vo.WxQrCodeFileVO;
import com.mall4j.cloud.common.cache.constant.BizCacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcodeTentacleStore;
import com.mall4j.cloud.biz.mapper.WeixinQrcodeTentacleStoreMapper;
import com.mall4j.cloud.biz.service.WeixinQrcodeTentacleStoreService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.common.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.mall4j.cloud.common.util.SpringContextUtils.applicationContext;

/**
 * 微信触点门店二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
@Slf4j
@Service
public class WeixinQrcodeTentacleStoreServiceImpl implements WeixinQrcodeTentacleStoreService {

    @Autowired
    private WeixinQrcodeTentacleStoreMapper weixinQrcodeTentacleStoreMapper;

    @Autowired
    private WeixinQrcodeTentacleStoreItemMapper weixinQrcodeTentacleStoreItemMapper;

    @Autowired
    private QrcodeTicketService qrcodeTicketService;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private WeixinQrcodeTentacleService weixinQrcodeTentacleService;
    @Autowired
    private WxConfig wxConfig;

    /**
     * 缓存
     */
    private static final Cache<Object, Object> QRCODE_TENTACLE_MAP = Caffeine.newBuilder()
            // 数量上限
            .maximumSize(100)
            // 过期机制
            .expireAfterWrite(5, TimeUnit.MINUTES).build();

    @Override
    public PageVO<WeixinQrcodeTentacleStore> page(PageDTO pageDTO) {
        PageVO<WeixinQrcodeTentacleStore> pageVO=PageUtil.doPage(pageDTO, () -> weixinQrcodeTentacleStoreMapper.list());
        return pageVO;
    }

    /**
     * 门店二维码列表
     * @param pageDTO
     * @param tentacleId
     * @return
     */
    @Override
    public PageVO<WeixinQrcodeTentacleStoreVO> storeCodePage(PageDTO pageDTO, String tentacleId) {
        PageVO<WeixinQrcodeTentacleStoreVO> pageVO=PageUtil.doPage(pageDTO, () -> weixinQrcodeTentacleStoreMapper.getList(tentacleId));

        pageVO.getList().stream().peek(item->{
            StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(item.getStoreId()));
            if(storeVO!=null){
                item.setStoreName(storeVO.getName());
                item.setStoreCode(storeVO.getStoreCode());
            }
        }).collect(Collectors.toList());

        return pageVO;
    }

    @Override
    public PageVO<WeixinQrcodeTentacleStoreItemVO> storeCodeItemPage(WeixinQrcodeTentacleStoreItemDTO pageDTO) {
        PageVO<WeixinQrcodeTentacleStoreItemVO> pageVO = PageUtil.doPage(pageDTO, () -> weixinQrcodeTentacleStoreItemMapper.selectTentacleStoreItemList(pageDTO));
        pageVO.getList().stream().peek(item->{
            StoreVO storeVO = storeFeignClient.findByStoreId(Long.parseLong(item.getStoreId()));
            if(storeVO != null){
                item.setStoreName(storeVO.getName());
                item.setStoreCode(storeVO.getStoreCode());
            }
        }).collect(Collectors.toList());
        return pageVO;
    }

    /**
     * 微信触点门店详情列表导出
     * @param param 微信触点门店详情列表导出参数
     * @return
     */
    @Override
    public String storeCodeItemExcel(WeixinQrcodeTentacleStoreItemDTO param) {
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(StoreCodeItemExport.STORE_CODE_ITEM_DETAIL_FILE_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downloadCenterId=null;
            if(serverResponseEntity.isSuccess()){
                downloadCenterId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            StoreCodeItemExport storeCodeItemExcel = new StoreCodeItemExport();
            storeCodeItemExcel.setWeixinQrcodeTentacleStoreItemDTO(param);
            storeCodeItemExcel.setDownloadCenterId(downloadCenterId);
            applicationContext.publishEvent(storeCodeItemExcel);

            return "操作成功，请转至下载中心下载";
        }catch (Exception e){
            log.error("微信触点门店详情列表导出错误: "+e.getMessage(),e);
            return "操作失败";
        }
    }

    /**
     * 微信触点门店列表批量导出时查询门店列表
     * @param param 微信触点门店列表批量导出时查询门店列表接口需求参数
     * @return
     */
    @Override
    public PageVO<WeixinQrcodeTentacleStoreItemExportVO> qrcodeTentacleStorePage(WeixinQrcodeTentacleStoreExportDTO param) {
        PageVO<WeixinQrcodeTentacleStoreItemExportVO> pageVO = PageUtil.doPage(param, () -> weixinQrcodeTentacleStoreMapper.getStoreListByTentacleIds(param.getTentacleStoreIds()));

        pageVO.getList().stream().peek(item ->{
            StoreVO storeVO = storeFeignClient.findByStoreId(Long.parseLong(item.getStoreId()));
            if(storeVO != null){
                item.setStoreName(storeVO.getName());
                item.setStoreCode(storeVO.getStoreCode());
            }
        }).collect(Collectors.toList());

        return pageVO;
    }

    /**
     * 微信触点门店列表批量导出
     * @param pageDTO 微信触点门店列表分页传参
     * @param tentacleIds 微信触点门店Id,多ID用逗号分隔
     * @param param 微信触点门店列表批量导出入参
     * @return
     */
    @Override
    public String qrcodeTentacleStoreExcel(WeixinQrcodeTentacleStoreExportDTO param) {
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(QrcodeTentacleStoreExport.QRCODE_TENTACLE_STORE_FILE_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downloadCenterId=null;
            if(serverResponseEntity.isSuccess()){
                downloadCenterId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            QrcodeTentacleStoreExport qrcodeTentacleStoreExport = new QrcodeTentacleStoreExport();
            qrcodeTentacleStoreExport.setWeixinQrcodeTentacleStoreExportDTO(param);
            qrcodeTentacleStoreExport.setDownloadCenterId(downloadCenterId);
            applicationContext.publishEvent(qrcodeTentacleStoreExport);

            return "操作成功，请转至下载中心下载";
        }catch (Exception e){
            log.error("微信触点门店详情列表导出错误: "+e.getMessage(),e);
            return "操作失败";
        }
    }

    @Override
    public WeixinQrcodeTentacleStore getById(Long id) {
        return weixinQrcodeTentacleStoreMapper.getById(id);
    }

    @Override
    public void save(WeixinQrcodeTentacleStore weixinQrcodeTentacleStore) {
        weixinQrcodeTentacleStoreMapper.save(weixinQrcodeTentacleStore);
    }

    @Override
    public void update(WeixinQrcodeTentacleStore weixinQrcodeTentacleStore) {
        weixinQrcodeTentacleStoreMapper.update(weixinQrcodeTentacleStore);
    }

    @Override
    public void deleteById(Long id) {
        weixinQrcodeTentacleStoreMapper.deleteById(id);
    }

//    @Override
//    public void saveTentacleStore(List<WxQrCodeFileVO> fileList, WeixinQrcodeTentacle qrcodeTentacle, String storeIds) {
//        String tentacleId=qrcodeTentacle.getId();
//        String path=qrcodeTentacle.getTentaclePath();
//        int codeWidth=430;
//        List<String> storeIdList= Arrays.asList(storeIds.split(","));
//        List<WeixinQrcodeTentacleStore> tentacleStores=new ArrayList<>();
//
//        //截取路径携带的参数
//        Map<String, String> paramsMap= URLUtil.urlSplit(path);
//
//        log.info("截取前 path："+path);
//        path=path.split("[?]")[0];
//        log.info("截取后 path："+path);
//        try {
//            int i=0;
//            for(String storeId:storeIdList){
//                i++;
//                //生成二维码
//                String scene="s="+NumberTo64.to64(Long.parseLong(storeId));
//                //添加多个参数
//                scene= WechatUtils.repScene(paramsMap,scene);
//                log.info("保存微信触点二维码 第【{}】个门店 scene【{}】",i,scene);
//
//                ServerResponseEntity<File> response= qrcodeTicketService.getWxaCodeFile(scene,path,codeWidth);
//                if(response.isSuccess()){
//                    StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(storeId));
//                    //上传二维码
//                    File file=response.getData();
//
//                    //生成的太阳码增加门店code信息
//                    File newFile= MaSunCodeUtils.ImgYin(storeVO.getStoreCode(),file.getPath());
//                    if(newFile!=null && newFile.isFile()){
//                        file=newFile;
//                    }
//
//                    FileInputStream fileInputStream = new FileInputStream(file);
//                    MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
//                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
//
//                    String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
//                    String originalFilename = multipartFile.getOriginalFilename();
//                    int pointIndex = -1;
//                    if (StringUtils.isNotBlank(originalFilename)) {
//                        pointIndex = originalFilename.lastIndexOf(".");
//                    }
//                    String ext = pointIndex != -1 ? originalFilename.substring(pointIndex) : "";
////                    StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(storeId));
//                    String name="_"+storeVO.getName()+"_"+storeVO.getStoreId()+"_";
//                    //time+id+门店名称+门店id
//                    String mimoPath = "wxqrcode/tentacle/" + time+name+(StringUtils.isBlank(ext) ? originalFilename : IdUtil.simpleUUID() + ext);
//                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
//                    if (responseEntity != null && responseEntity.isSuccess()) {
//                        //组合文件，直接打包zip上传，用于邮件发送
//                        WxQrCodeFileVO codeFileVO=new WxQrCodeFileVO();
//                        codeFileVO.setFile(response.getData());
//                        codeFileVO.setStoreName(storeVO.getName());
//                        codeFileVO.setStoreId(storeId);
//                        fileList.add(codeFileVO);
//
//                        WeixinQrcodeTentacleStore tentacleStore=new WeixinQrcodeTentacleStore();
//                        tentacleStore.setId(RandomUtil.getUniqueNumStr());
//                        tentacleStore.setCodeWidth(codeWidth);
//                        tentacleStore.setTentacleId(tentacleId);
//                        tentacleStore.setPath(path);
//                        tentacleStore.setScene(scene);
//                        tentacleStore.setStoreId(storeId);
////                        tentacleStore.setQrcodePath(responseEntity.getData());
//                        URLEncoder urlEncoder= URLEncoder.createDefault();
//                        urlEncoder.removeSafeCharacter('+');
//                        tentacleStore.setQrcodePath(urlEncoder.encode(responseEntity.getData(), Charset.forName("UTF-8")));
//                        tentacleStore.setStatus(0);
//                        tentacleStore.setCreateTime(new Date());
//                        tentacleStore.setDelFlag(0);
//
//                        tentacleStores.add(tentacleStore);
//                    }
//
//                }else{
//                    log.info(storeId+"-->触点门店二维码生成失败-->"+response.getMsg());
//                }
//            }
//        }catch (Exception e){
//            log.info("保存微信触点二维码2 操作失败 【{}】 【{}】",e,e.getMessage());
//            throw new LuckException("操作失败");
//        }
//        if(CollectionUtil.isNotEmpty(tentacleStores)){
//            weixinQrcodeTentacleStoreMapper.saveBatch(tentacleStores);
//        }
//    }

    @Override
    public void saveTentacleStore(WeixinQrcodeTentacle qrcodeTentacle, String storeIds, Long downHisId) {
        String tentacleId=qrcodeTentacle.getId();
        String path=qrcodeTentacle.getTentaclePath();
        int codeWidth=1280;
        List<String> storeIdList= Arrays.asList(storeIds.split(","));
        List<WeixinQrcodeTentacleStore> tentacleStores=new ArrayList<>();

        //下载中心记录
        FinishDownLoadDTO finishDownLoadDTO=new FinishDownLoadDTO();
        finishDownLoadDTO.setId(downHisId);

        //截取路径携带的参数
        Map<String, String> paramsMap= URLUtil.urlSplit(path);

        List<WxQrCodeFileVO> fileList=new ArrayList<>();

        log.info("截取前 path："+path);
        path=path.split("[?]")[0];
        log.info("截取后 path："+path);

        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";
        new File(zipFilePathExport).mkdirs();

        try {
            int i=0;
            for(String storeId:storeIdList){
                i++;
                //生成二维码
                String scene="s="+NumberTo64.to64(Long.parseLong(storeId));
                //添加多个参数
                scene= WechatUtils.repScene(paramsMap,scene);
                log.info("保存微信触点二维码 第【{}】个门店 scene【{}】",i,scene);
                // 修改触点链接地址，新增参数
                String tentacleStoreId = RandomUtil.getUniqueNumStr();
                StringBuffer tentacleSign = new StringBuffer();
                tentacleSign.append("&l0=");
                tentacleSign.append(NumberTo64.to64(Long.parseLong(tentacleStoreId)));
                //tentacleSign.append("&sc=");
                //tentacleSign.append( SceneType.TENTACLE.getValue());
                scene += tentacleSign;
                log.info("保存微信触点二维码 第【{}】个门店 scene【{}】",i,scene);

//                ServerResponseEntity<File> response= qrcodeTicketService.getWxaCodeFile(scene,path,codeWidth);
                ServerResponseEntity<byte[]> response= qrcodeTicketService.getWxaCodeByte(scene,path,codeWidth);
                if(response.isSuccess() && Objects.nonNull(response.getData())){

                    StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(storeId));
                    if(Objects.isNull(storeVO) || Objects.isNull(storeVO.getStoreCode())){
                        continue;
                    }
                    String name=zipFilePathExport+storeVO.getStoreCode()+"_"+storeVO.getName()+"_"+System.currentTimeMillis()+".png";

                    File file=FileUtil.writeBytes(response.getData(),name);
//                    File file=response.getData();

                    //生成的太阳码增加门店code信息
                    File newFile= MaSunCodeUtils.ImgYin(storeVO.getStoreCode(),file.getPath(),220,34);
                    if(newFile!=null && newFile.isFile()){
                        file=newFile;
                    }

                    FileInputStream fileInputStream = new FileInputStream(file);
                    MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                            ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

//                    String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    String originalFilename = multipartFile.getOriginalFilename();
                    int pointIndex = -1;
                    if (StringUtils.isNotBlank(originalFilename)) {
                        pointIndex = originalFilename.lastIndexOf(".");
                    }
                    String ext = pointIndex != -1 ? originalFilename.substring(pointIndex) : "";
//                    String name=storeVO.getStoreCode()+"_"+storeVO.getName()+"_"+System.currentTimeMillis()+ext;
                    //time+id+门店名称+门店id
//                    String mimoPath = "wxqrcode/tentacle/" + time+"/"+name+(StringUtils.isBlank(ext) ? originalFilename : IdUtil.simpleUUID() + ext);
                    String mimoPath = "wxqrcode/tentacle/" + time+name;
                    ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                    if (responseEntity != null && responseEntity.isSuccess()) {
                        log.info("门店{} {} 触点码上传后地址{}",storeVO.getStoreCode(),storeVO.getName(),responseEntity.getData());
                        //组合文件，直接打包zip上传，用于邮件发送
                        WxQrCodeFileVO codeFileVO=new WxQrCodeFileVO();
                        codeFileVO.setFile(file);
                        codeFileVO.setFilePath(zipFilePathExport);
                        codeFileVO.setStoreName(storeVO.getName());
                        codeFileVO.setStoreId(storeId);
                        codeFileVO.setStoreCode(storeVO.getStoreCode());
                        fileList.add(codeFileVO);

                        WeixinQrcodeTentacleStore tentacleStore=new WeixinQrcodeTentacleStore();
                        tentacleStore.setId(tentacleStoreId);
                        tentacleStore.setCodeWidth(codeWidth);
                        tentacleStore.setTentacleId(tentacleId);
                        tentacleStore.setPath(path);
                        tentacleStore.setScene(scene);

                        tentacleStore.setStoreId(storeId);
//                        tentacleStore.setQrcodePath(responseEntity.getData());
                        URLEncoder urlEncoder= URLEncoder.createDefault();
                        urlEncoder.removeSafeCharacter('+');
                        tentacleStore.setQrcodePath(urlEncoder.encode(responseEntity.getData(), Charset.forName("UTF-8")));
                        tentacleStore.setStatus(0);
                        tentacleStore.setCreateTime(new Date());
                        tentacleStore.setDelFlag(0);

                        tentacleStores.add(tentacleStore);
                    }

                }else{
                    log.info(storeId+"-->触点门店二维码生成失败-->"+response.getMsg());
                }
            }
        }catch (Exception e){
            log.info("保存微信触点二维码2 操作失败 【{}】 【{}】",e,e.getMessage());
            throw new LuckException("操作失败");
        }
        if(CollectionUtil.isNotEmpty(tentacleStores)){

            weixinQrcodeTentacleStoreMapper.saveBatch(tentacleStores);

            filesToZip(qrcodeTentacle,fileList,finishDownLoadDTO,zipFilePathExport);
        }
    }

    public static void main(String[] s){
//        String url="https://xcx-prd.oss-cn-shanghai.aliyuncs.com/wxqrcode/tentacle/20220721//opt/skechers/temp/tentacle/20220721112041/NB0102_RETAIL北京市上品+KIDS_1658373641437.png";
        String url="https://xcx-sit-uat.oss-cn-shanghai.aliyuncs.com/wxqrcode/tentacle/20220721/opt/skechers/temp/tentacle/20220721165529/ZJ-ZY-OL-02_浙江-自营OUTLET_1658393729719.png";

        URLEncoder urlEncoder= URLEncoder.createDefault();
        urlEncoder.removeSafeCharacter('+');
        System.out.println(urlEncoder.encode(url, Charset.forName("UTF-8")));

    }

    private void filesToZip(WeixinQrcodeTentacle weixinQrcodeTentacle,List<WxQrCodeFileVO> codeFileVOS,FinishDownLoadDTO finishDownLoadDTO,String zipFilePathExport){
        if(CollectionUtil.isEmpty(codeFileVOS)){
            finishDownLoadDTO.setRemarks("失败，生成码文件集合为空");
            finishDownLoadDTO.setStatus(2);
            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
            return;
        }
        if(CollectionUtil.isNotEmpty(codeFileVOS)){
            try {
                int calCount=codeFileVOS.size();
//                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                String zipFilePathExport="/opt/skechers/temp/tentacle/"+time+"/";

                File copyFile=new File(zipFilePathExport);
//                copyFile.mkdirs();

//                List<File> fileList=new ArrayList<>();
                //文件存放统一目录
//                copyCodeToFile(codeFileVOS,zipFilePathExport,fileList);

//                log.info("保存微信触点二维码 fileList【{}】",fileList.size());
                log.info("保存微信触点二维码 fileList【{}】",codeFileVOS.size());

                if(CollectionUtil.isEmpty(codeFileVOS)){
                    finishDownLoadDTO.setRemarks("失败，文件集合为空");
                    finishDownLoadDTO.setStatus(2);
                    downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                    return;
                }

//                if(CollectionUtil.isNotEmpty(fileList)){
                if(CollectionUtil.isNotEmpty(codeFileVOS)){
                    //压缩统一文件目录
                    String zipPath= CompressUtil.zipFile(copyFile,"zip");
                    log.info("保存微信触点二维码 zip zipPath【{}】",zipPath);
                    if(new File(zipPath).isFile()){
                        File zipFile=new File(zipPath);
                        FileInputStream fileInputStream = new FileInputStream(zipFile);
                        MultipartFile multipartFile = new MultipartFileDto(zipFile.getName(), zipFile.getName(),
                                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                        String originalFilename = multipartFile.getOriginalFilename();
                        int pointIndex = -1;
                        if (StringUtils.isNotBlank(originalFilename)) {
                            pointIndex = originalFilename.lastIndexOf(".");
                        }
                        String mimoPath = "wxqrcode/tentacle/" + originalFilename;
                        ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                        if(responseEntity.isSuccess()){
                            log.info("保存微信触点二维码 zip url 【{}】",responseEntity.getData());
                            weixinQrcodeTentacle.setCodeZipPath(responseEntity.getData());
                            weixinQrcodeTentacle.setUpdateTime(new Date());
                            weixinQrcodeTentacleService.update(weixinQrcodeTentacle);

                            //下载中心记录
                            finishDownLoadDTO.setCalCount(calCount);
                            finishDownLoadDTO.setStatus(1);
                            finishDownLoadDTO.setFileUrl(responseEntity.getData());
                            finishDownLoadDTO.setRemarks("保存微信触点二维码成功");
                            downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);
                        }
                        //删除本地文件
                        File file=new File(zipPath);
                        FileUtil.clean(file);
                        FileUtil.clean(copyFile);
                        file.delete();
                        copyFile.delete();
                    }
                }

            }catch (Exception e){
                log.info("保存微信触点二维码1 操作失败 【{}】 【{}】",e,e.getMessage());
                finishDownLoadDTO.setRemarks("保存微信触点码报错失败");
                finishDownLoadDTO.setStatus(2);
                downloadCenterFeignClient.finishDownLoad(finishDownLoadDTO);

                File zipFile=new File(zipFilePathExport);
                FileUtil.clean(zipFile);
                zipFile.delete();

                throw new LuckException("操作失败");
            }
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
                String saveFileName=zipFilePathExport+""+codeFileVO.getStoreCode()+"_"+codeFileVO.getStoreName()+"_"+System.currentTimeMillis()+ext;
                log.info("保存微信触点二维码 文件复制原路径【{}】 新路径【{}】",codeFileVO.getFile().getPath(),saveFileName);
                File outFile=new File(saveFileName);
                FileCopyUtils.copy(codeFileVO.getFile(),outFile);
                fileList.add(outFile);
                //删除本地文件
                FileUtil.clean(codeFileVO.getFile());
                codeFileVO.getFile().delete();
            }
        }catch (IOException e){
            log.info("保存微信触点二维码 文件复制新路径报错 {} {}",e,e.getMessage());
        }
    }

    @Override
    public ServerResponseEntity<String> saveQrcodeTentacleStoreItem(String tentacleStoreId, String code) throws WxErrorException {
        //UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        int num = 0;

        log.info("新增触点详情记录 微信Code【{}】",code);
        code = code.replace("\"", "");
        WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(code);
        log.info("小程序登录，获取用户session【{}】",session != null ? JSON.toJSONString(session):"未获取到session信息");
        String qrcodeTentacleStoreId = NumberTo64.form64(tentacleStoreId).toString();

        WeixinQrcodeTentacleStoreItem weixinQrcodeTentacleStoreItem = new WeixinQrcodeTentacleStoreItem();
        String unionId = session.getUnionid();
        weixinQrcodeTentacleStoreItem.setUniId(unionId);
        weixinQrcodeTentacleStoreItem.setTentacleStoreId(qrcodeTentacleStoreId);
        weixinQrcodeTentacleStoreItem.setCheckTime(new Date());
        UserApiVO user = userFeignClient.getUserByUnionId(unionId).getData();
        if(Objects.nonNull(user)){
            weixinQrcodeTentacleStoreItem.setNickName(user.getNickName());
            weixinQrcodeTentacleStoreItem.setVipCode(user.getVipcode());
        }

        // 当详情表新增数据成功之后，触点列表记录表中的点击人数和次数要进行递增
        num = weixinQrcodeTentacleStoreMapper.selectQrcodeTentacleStoreItemOfUserId(unionId, qrcodeTentacleStoreId);
        int result = weixinQrcodeTentacleStoreItemMapper.insert(weixinQrcodeTentacleStoreItem);
        if (result == 0) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }

        List<WeixinQrcodeTentacleStore> weixinQrcodeTentacleStores = weixinQrcodeTentacleStoreMapper.selectList(new QueryWrapper<WeixinQrcodeTentacleStore>().lambda()
                .eq(WeixinQrcodeTentacleStore::getTentacleId, qrcodeTentacleStoreId)
        );
        if(Objects.isNull(weixinQrcodeTentacleStores)){
            return ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID, "数据异常，并未找到对应的触点记录");
        }

        weixinQrcodeTentacleStoreMapper.updateByTentacleStoreId(qrcodeTentacleStoreId, num);
        return ServerResponseEntity.success("短链详情记录新增成功");
    }

    /**
     * 获取微信触点二维码信息查缓存
     * @param tentacleStoreId 微信门店触点二维码ID
     * @return
     */
    @Override
    public ServerResponseEntity<WeixinQrcodeTentacleStoreVO> getQrcodeTentacleStoreScene(String tentacleStoreId) {
        WeixinQrcodeTentacleStoreVO weixinQrcodeTentacleStoreVO = (WeixinQrcodeTentacleStoreVO) QRCODE_TENTACLE_MAP.get(tentacleStoreId, cacheKey -> {
            WeixinQrcodeTentacleStoreVO qrcodeTentacleStoreSceneFromDB = this.getQrcodeTentacleStoreSceneFromDB(tentacleStoreId);
            return qrcodeTentacleStoreSceneFromDB;
        });

        if (weixinQrcodeTentacleStoreVO == null) {
            QRCODE_TENTACLE_MAP.invalidate(tentacleStoreId);
        }
        return ServerResponseEntity.success(weixinQrcodeTentacleStoreVO);
    }

    /**
     * 获取微信触点二维码信息查数据库
     * @param tentacleStoreId 微信门店触点二维码ID
     * @return
     */
    @Cacheable(cacheNames = BizCacheNames.WECHAT_QRCODE_TENTACLE_KEY, key = "#tentacleStoreId")
    public WeixinQrcodeTentacleStoreVO getQrcodeTentacleStoreSceneFromDB(String tentacleStoreId) {
        WeixinQrcodeTentacleStore tentacleStore = weixinQrcodeTentacleStoreMapper.getById(Long.parseLong(tentacleStoreId));
        WeixinQrcodeTentacleStoreVO weixinQrcodeTentacleStoreVO = new WeixinQrcodeTentacleStoreVO();
        BeanUtils.copyProperties(tentacleStore, weixinQrcodeTentacleStoreVO);
        return weixinQrcodeTentacleStoreVO;
    }

}
