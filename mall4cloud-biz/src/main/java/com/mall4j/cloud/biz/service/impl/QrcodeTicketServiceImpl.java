package com.mall4j.cloud.biz.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.binarywang.wx.miniapp.bean.shortlink.GenerateShortLinkRequest;
import cn.binarywang.wx.miniapp.bean.urllink.GenerateUrlLinkRequest;
import cn.hutool.core.io.FileUtil;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.biz.feign.MinioUploadFeignClient;
import com.mall4j.cloud.biz.wx.wx.util.MaSunCodeUtils;
import com.mall4j.cloud.biz.wx.wx.util.QRUtil;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.vo.QrcodeTicketVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.QrcodeTicket;
import com.mall4j.cloud.biz.mapper.QrcodeTicketMapper;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 二维码数据信息
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
@Slf4j
@Service
public class QrcodeTicketServiceImpl implements QrcodeTicketService {

    @Autowired
    private QrcodeTicketMapper qrcodeTicketMapper;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private MinioUploadFeignClient minioUploadFeignClient;

    @Override
    public PageVO<QrcodeTicket> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> qrcodeTicketMapper.list());
    }

    @Override
    public QrcodeTicket getByTicketId(Long ticketId) {
        return qrcodeTicketMapper.getByTicketId(ticketId);
    }

    @Override
    public void save(QrcodeTicket qrcodeTicket) {
        qrcodeTicketMapper.save(qrcodeTicket);
    }

    @Override
    public void update(QrcodeTicket qrcodeTicket) {
        qrcodeTicketMapper.update(qrcodeTicket);
    }

    @Override
    public void deleteById(Long ticketId) {
        qrcodeTicketMapper.deleteById(ticketId);
    }

    @Override
    public QrcodeTicketVO getByTicket(String ticket) {
        return qrcodeTicketMapper.getByTicket(ticket);
    }

    /**
     * 小程序短链生成
     * @param path
     * @param isExpire
     * @return
     */
    @Override
    public ServerResponseEntity<String> generateUrlLink(String path, boolean isExpire,String id,Integer expireTime) {
        try {
            if(isExpire && expireTime==null){
                return ServerResponseEntity.showFailMsg("失效时间不能为空");
            }
            String urlLink= getUrlLink(path,isExpire,id,expireTime);
            log.info(""+urlLink);
            if(StringUtils.isNotEmpty(urlLink)){
                urlLink=urlLink.replace("\"","");
            }
            return ServerResponseEntity.success(urlLink);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    @Override
    public ServerResponseEntity<String> generateShortLink(String path, String id) {
        try {
            String urlLink= getGenerateShortLink(path,id);
            log.info(""+urlLink);
            if(StringUtils.isNotEmpty(urlLink)){
                urlLink=urlLink.replace("\"","");
            }
            return ServerResponseEntity.success(urlLink);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 小程序短链生成(链接转二维码，二维码图片base64编码)
     * @param path
     * @param isExpire
     * @return
     */
    @Override
    public ServerResponseEntity<String> generateQrcodeUrlLink(String path, boolean isExpire, String id, Integer qrcodeSize,Integer expireTime) {
        try {
            if(isExpire && expireTime==null){
                return ServerResponseEntity.showFailMsg("失效时间不能为空");
            }
            String urlLink= getUrlLink(path,isExpire,id,expireTime);
            log.info(""+urlLink);
            String qrcodeData=null;
            if(StringUtils.isNotEmpty(urlLink)){
                urlLink=urlLink.replace("\"","");
                //链接转二维码
                BufferedImage bufferedImage= QRUtil.encodeLogo(urlLink,null,false,qrcodeSize);
                //二维码base64编码
                qrcodeData=QRUtil.convert(bufferedImage);
            }
            return ServerResponseEntity.success(qrcodeData);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 生成小程序太阳码(Base64图片)
     * @param scene 参数
     * @param path  页面路径
     * @return
     */
    @Override
    public ServerResponseEntity<String> getWxaCodeBaseImg(String scene, String path,Integer width) {
        try {
            byte[] file=createWxaCodeUnlimitBytes(scene,path,width);
            //二维码base64编码
            String qrcodeData=QRUtil.convertByte(file);
            return ServerResponseEntity.success(qrcodeData);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("生成失败："+e.getMessage());
        }
    }

    @Override
    public ServerResponseEntity<String> getBase64InsertCode(String scene, String path, Integer width, Integer width_y,String code) {
        try {
            width_y=Objects.nonNull(width_y)?width_y:105;
            File file=createWxaCodeUnlimitFile(scene,path,width);
            File newFile= MaSunCodeUtils.ImgYin("  "+code,file.getPath(), width_y,0);
            //二维码base64编码
            String qrcodeData=QRUtil.convertByte(FileUtil.readBytes(newFile));
            return ServerResponseEntity.success(qrcodeData);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("生成失败："+e.getMessage());
        }
    }

    /**
     * 生成小程序太阳码(本地文件路径)
     * @param scene 参数
     * @param path  页面路径
     * @return
     */
    @Override
    public ServerResponseEntity<File> getWxaCodeFile(String scene, String path, Integer width) {
        try {
            return ServerResponseEntity.success(createWxaCodeUnlimitFile(scene,path,width));
        }catch (WxErrorException e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("生成失败："+e.getMessage());
        }
    }

    @Override
    public ServerResponseEntity<byte[]> getWxaCodeByte(String scene, String path, Integer width) {
        try {
            byte[] file=createWxaCodeUnlimitBytes(scene,path,width);
            return ServerResponseEntity.success(file);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("生成失败："+e.getMessage());
        }
    }

    @Override
    public ServerResponseEntity<String> getWxaCodeUrl(String scene, String path, Integer width,Long storeId) {
        try {
            //截取路径携带的参数
            //生成二维码
            if(storeId!=null){
                scene=scene+"&s="+ NumberTo64.to64(storeId);
            }

            File file=createWxaCodeUnlimitFile(scene,path,width);
            if(file.isFile()){

                FileInputStream fileInputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MultipartFileDto(file.getName(), file.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String originalFilename = multipartFile.getOriginalFilename();
                int pointIndex = -1;
                if (StringUtils.isNotBlank(originalFilename)) {
                    pointIndex = originalFilename.lastIndexOf(".");
                }
                String mimoPath = "wxqrcode/macodeimg/" + System.currentTimeMillis()+ RandomUtil.getRandomStr(3) ;
                ServerResponseEntity<String> responseEntity = minioUploadFeignClient.minioFileUpload(multipartFile, mimoPath, multipartFile.getContentType());
                return responseEntity;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("生成失败："+e.getMessage());
        }
    }

    /**
     * 获取小程序短链接
     * @param path
     * @param isExpire
     * @param id
     * @return
     * @throws WxErrorException
     */
    private String getUrlLink(String path,boolean isExpire,String id,Integer expireTime) throws WxErrorException {
        GenerateUrlLinkRequest.GenerateUrlLinkRequestBuilder builder = GenerateUrlLinkRequest.builder();
        builder.path(path);
        builder.isExpire(isExpire);
        if(isExpire){//到期失效：true，永久有效：false
            builder.expireType(0);
            builder.expireTime(expireTime.longValue());
        }
        if(StringUtils.isNotEmpty(id)){
            builder.query(id);
        }
        GenerateUrlLinkRequest request = builder.build();
        String urlLink= wxConfig.getWxMaService().getLinkService().generateUrlLink(request);
        return urlLink;
    }

    private String getGenerateShortLink(String path,String id) throws WxErrorException {
        GenerateShortLinkRequest.GenerateShortLinkRequestBuilder builder = GenerateShortLinkRequest.builder();
        path=path+"?query="+id;
        builder.pageUrl(path);
        builder.pageTitle("首页");
        builder.isPermanent(true);
        GenerateShortLinkRequest request = builder.build();
        String urlLink= wxConfig.getWxMaService().getLinkService().generateShortLink(request);
        return urlLink;
    }

    /**
     * 小程序太阳码
     * @param scene 参数
     * @param path  页面路径
     * @return
     * @throws WxErrorException
     */
    private byte[] createWxaCodeUnlimitBytes(String scene,String path,Integer codeWidth)throws WxErrorException{
        int width=codeWidth!=null&&codeWidth>0?codeWidth:430;//二维码的宽度，单位 px，最小 280px，最大 1280px
        boolean autoColor=false;//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false
        boolean isHyaline=false;//是否需要透明底色，为 true 时，生成透明底色的小程序
        boolean check_path=true;//默认是true，检查page 是否存在
        String env_version=null;//要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版
        WxMaCodeLineColor lineColor=new WxMaCodeLineColor("0","0","0");//auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
        byte[] bytes=wxConfig.getWxMaService().getQrcodeService().createWxaCodeUnlimitBytes(scene,path,check_path,env_version,width,autoColor,lineColor,isHyaline);
        return bytes;
    }

    private File createWxaCodeUnlimitFile(String scene,String path,Integer codeWidth)throws WxErrorException{
        int width=codeWidth!=null&&codeWidth>0?codeWidth:430;//二维码的宽度，单位 px，最小 280px，最大 1280px
        boolean autoColor=false;//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false
        boolean isHyaline=false;//是否需要透明底色，为 true 时，生成透明底色的小程序
        boolean check_path=true;//默认是true，检查page 是否存在
        String env_version=null;//要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版
        WxMaCodeLineColor lineColor=new WxMaCodeLineColor("0","0","0");//auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
        File file=wxConfig.getWxMaService().getQrcodeService().createWxaCodeUnlimit(scene,path,check_path,env_version,width,autoColor,lineColor,isHyaline);
        return file;
    }

}
