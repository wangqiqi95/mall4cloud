package com.mall4j.cloud.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.biz.wx.wx.util.QRUtil;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.WeixinQrcodePutDTO;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinQrcode;
import com.mall4j.cloud.biz.mapper.WeixinQrcodeMapper;
import com.mall4j.cloud.biz.service.WeixinQrcodeService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.error.YAMLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 微信二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
@Slf4j
@Service
public class WeixinQrcodeServiceImpl implements WeixinQrcodeService {

    @Autowired
    private WeixinQrcodeMapper weixinQrcodeMapper;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private WeixinWebAppService weixinWebAppService;

    @Override
    public PageVO<WeixinQrcodeVO> page(PageDTO pageDTO, String actionInfo, String storeId, String startTime, String endTime) {
        Date startDate= !StringUtils.isEmpty(startTime)?DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss"):null;
        Date endDate= !StringUtils.isEmpty(endTime)?DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss"):null;
        List storeIds=StringUtils.isNotEmpty(storeId)?Arrays.asList(storeId.split(",")):null;
        PageVO<WeixinQrcodeVO> pageVO=PageUtil.doPage(pageDTO, () -> weixinQrcodeMapper.getList(actionInfo, storeIds,startDate,endDate));

        pageVO.getList().stream().peek(item->{

            //门店信息
            if(StringUtils.isNotEmpty(item.getStoreId())){
//                StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(item.getStoreId()));
//                if(storeVO!=null){
//                    item.setStaffName(storeVO.getName());
//                }
                ServerResponseEntity<StaffVO>  staffVO=staffFeignClient.getStaffById(Long.parseLong(item.getStoreId()));
                if(staffVO!=null && staffVO.isSuccess() && Objects.nonNull(staffVO.getData())){
                    item.setStaffName(staffVO.getData().getStaffName());
                }
            }

        }).collect(Collectors.toList());

        return pageVO;
    }

    @Override
    public WeixinQrcode getById(String id) {
        return weixinQrcodeMapper.getById(id);
    }

    @Override
    public void save(WeixinQrcode weixinQrcode) {
        weixinQrcodeMapper.save(weixinQrcode);
    }

    @Override
    public void update(WeixinQrcode weixinQrcode) {
        weixinQrcodeMapper.update(weixinQrcode);
    }

    @Override
    public void deleteById(String id) {
        weixinQrcodeMapper.deleteById(id);
    }

    @Override
    public Integer getScene(String appId, String actionName) {
        int maxScenekey = 0;
        //场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
        Integer max =weixinQrcodeMapper.queryMaxSceneId(appId,actionName);
        synchronized (WeixinQrcodeServiceImpl.class) {
            if(max == null){
                maxScenekey = 1;
            }else{
                maxScenekey = max.intValue()+1;
            }
            // 临时二维码Scenceid从10w以后开始
            if(maxScenekey<=100000 && "QR_SCENE".equals(actionName)){
                maxScenekey = 100001;
            }
        }
        return maxScenekey;
    }

    @Override
    public void saveWeixinQrcode(WeixinQrcodePutDTO qrcodePutDTO) {
        String id=qrcodePutDTO.getId();
        WeixinQrcode qrcode= StringUtils.isNotEmpty(id)?getById(id):null;
        if(qrcode==null){

            //校验名称是否存在
            if(Objects.nonNull(weixinQrcodeMapper.getByActionInfo(qrcodePutDTO.getActionInfo(),qrcodePutDTO.getAppId()))){
                throw new LuckException("作业名称【"+qrcodePutDTO.getActionInfo()+"】已存在");
            }
            if(qrcodePutDTO.getContactType()==1){
                if(StrUtil.isEmpty(qrcodePutDTO.getContactStr())){
                    throw new LuckException("自定义渠道触点内容不能为空");
                }
                //检验自定义触点是否存在
                if(Objects.nonNull(weixinQrcodeMapper.getByContactStr(qrcodePutDTO.getContactStr(),qrcodePutDTO.getAppId()))){
                    throw new LuckException("自定义渠道触点【"+qrcodePutDTO.getContactStr()+"】已存在");
                }
                qrcode=new WeixinQrcode();
                BeanUtils.copyProperties(qrcodePutDTO,qrcode);
                qrcode.setContactStr(qrcodePutDTO.getContactStr());
                qrcode.inint();
                Integer sceneId = this.getScene(qrcode.getAppId(),qrcode.getActionName());
                qrcode.setSceneId(sceneId);
                //有效期类型： 1永久 2临时
                if(qrcode.getIsExpire()==2){
                    qrcode.setExpireSeconds(2592000);
                    qrcode.setActionName("QR_SCENE");
                }else{
                    qrcode.setActionName("QR_LIMIT_SCENE");
                }
                qrcode.setStatus(1);
                this.save(qrcode);
                qrcode=this.getById(qrcode.getId());
                if(qrcode!=null){//保存即刻生成二维码
                    generateQrcode(qrcode);
                }
            }else{
                //多个门店ID，批量生成二维码，一个门店id对应一个二维码
                String[] storeIds=qrcodePutDTO.getStoreId().split(",");
                for(int i=0;i<storeIds.length;i++){
                    qrcode=new WeixinQrcode();
                    //TODO 校验员工是有已有二维码
                    String staffId=storeIds[i];
                    if(Objects.nonNull(weixinQrcodeMapper.getByStaff(staffId,qrcodePutDTO.getAppId()))){
                        throw new LuckException("员工id【"+staffId+"】已有二维码");
                    }
                    BeanUtils.copyProperties(qrcodePutDTO,qrcode);
                    qrcode.setStoreId(staffId);
                    qrcode.inint();
                    Integer sceneId = this.getScene(qrcode.getAppId(),qrcode.getActionName());
                    qrcode.setSceneId(sceneId);
                    //有效期类型： 1永久 2临时
                    if(qrcode.getIsExpire()==2){
                        qrcode.setExpireSeconds(2592000);
                        qrcode.setActionName("QR_SCENE");
                    }else{
                        qrcode.setActionName("QR_LIMIT_SCENE");
                    }
                    qrcode.setStatus(1);
                    this.save(qrcode);
                    qrcode=this.getById(qrcode.getId());
                    if(qrcode!=null){//保存即刻生成二维码
                        generateQrcode(qrcode);
                    }
                }
            }
        }else{
//            BeanUtils.copyProperties(qrcodePutDTO,qrcode);
//            qrcode.setUpdateTime(new Date());
//            this.update(qrcode);
        }
    }

    @Override
    public ServerResponseEntity<WeixinQrcode> generateQrcode(WeixinQrcode weixinQrcode) {
        try {
            //获取授权公众号信息
            WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(weixinQrcode.getAppId());
            if(weixinWebApp==null){
                log.info("WxScanEventHandler 公众号未授权 --->"+weixinQrcode.getAppId());
                return ServerResponseEntity.showFailMsg("公众号未授权");
            }
            WxMp wxMp = new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken = wxConfig.getWxMpService(wxMp).getAccessToken();
            if(StringUtils.isEmpty(accessToken)){
                return ServerResponseEntity.showFailMsg("未获取到公众号accessToken");
            }
            //有效期类型： 1永久 2临时
            String scene_id=weixinQrcode.getStoreId().toString();
            if(weixinQrcode.getContactType()==1){
                scene_id=weixinQrcode.getContactStr();
            }
            WxMpQrCodeTicket qrCodeTicket=null;
            if(weixinQrcode.getIsExpire()==1){
                qrCodeTicket=wxConfig.getWxMpService(wxMp).getQrcodeService().qrCodeCreateLastTicket(scene_id);
            }else{
                qrCodeTicket=wxConfig.getWxMpService(wxMp).getQrcodeService().qrCodeCreateTmpTicket(scene_id,weixinQrcode.getExpireSeconds());
            }
            log.info(""+qrCodeTicket.toString());
            if(qrCodeTicket!=null && StringUtils.isNotEmpty(qrCodeTicket.getTicket())){
                String qrcodeimgurl=wxConfig.getWxMpService(wxMp).getQrcodeService().qrCodePictureUrl(qrCodeTicket.getTicket());
                weixinQrcode.setQrcodeUrl(qrcodeimgurl);
                weixinQrcode.setTicket(qrCodeTicket.getTicket());
                weixinQrcode.setUrl(qrCodeTicket.getUrl());
                if(weixinQrcode.getActionName().equals("QR_SCENE")){
                    weixinQrcode.setExpireSeconds(weixinQrcode.getExpireSeconds());
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND,weixinQrcode.getExpireSeconds());
                    weixinQrcode.setExpireDate(cal.getTime());//有效期时间
                }
                weixinQrcode.setUpdateTime(new Date());
                this.update(weixinQrcode);
                return ServerResponseEntity.success(weixinQrcode);
            }else{
                return ServerResponseEntity.showFailMsg("生成失败");
            }
        }catch (Exception e){
            log.info(""+e.getMessage());
            return ServerResponseEntity.showFailMsg("生成失败");
        }
    }

    @Override
    public void downQrcode(String id, int qrcodeSize, HttpServletRequest request, HttpServletResponse response) {
        WeixinQrcode weixinQrcode=getById(id);
        if(weixinQrcode==null || StringUtils.isEmpty(weixinQrcode.getQrcodeUrl()) || weixinQrcode.getStatus()!=1){
            throw new LuckException("下载失败");
        }
        try {
//            String content=weixinQrcode.getQrcodeUrl();
            String content=weixinQrcode.getUrl();
            String logoUrl=weixinQrcode.getLogoUrl();
            String topText=null;
            String upText=null;
//            StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(weixinQrcode.getStoreId()));
//            String topText="scrm";
//            String upText=storeVO.getName()+" | "+storeVO.getStoreCode();
            int spLength=14;
            if(qrcodeSize==900){
                spLength=16;
            }
            if(qrcodeSize==1500){
                spLength=20;
            }
//            upText=QRUtil.splitStrLength(upText,spLength);
//            BufferedImage bufferedImage=QRUtil.encodeLogo(content,logoUrl,true,qrcodeSize);
            BufferedImage bufferedImage=QRUtil.createImageAndLogoText(content,logoUrl,topText,upText,true,qrcodeSize);
            ImageIO.write(bufferedImage, QRUtil.FORMAT_NAME, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
            throw new LuckException("下载失败");
        }
    }

    @Override
    public void downQrcodeAll(String ids, int qrcodeSize, HttpServletRequest request, HttpServletResponse response) {
        List<WeixinQrcode> weixinQrcodes=weixinQrcodeMapper.getDownList(ids);
        if(weixinQrcodes.isEmpty()){
            throw new YAMLException("下载失败");
        }
        try {
            String zipName=new String("二维码".getBytes(),"ISO-8859-1") + ".zip";
            response.setContentType("application/zip");
            response.setHeader("Content-disposition","attachment; filename="+zipName);

            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            for(WeixinQrcode weixinQrcode:weixinQrcodes){
//                String content=weixinQrcode.getQrcodeUrl();
                String content=weixinQrcode.getUrl();
                String logoUrl=weixinQrcode.getLogoUrl();
                if(StringUtils.isNotEmpty(content) && weixinQrcode.getStatus()==1){
                    String topText=null;
                    String upText=null;
//                    StoreVO storeVO=storeFeignClient.findByStoreId(Long.parseLong(weixinQrcode.getStoreId()));
//                    String topText="scrm";
//                    String upText=storeVO.getName()+" | "+storeVO.getStoreCode();
//                    int spLength=14;
//                    if(qrcodeSize==900){
//                        spLength=16;
//                    }
//                    if(qrcodeSize==1500){
//                        spLength=20;
//                    }
//                    upText=QRUtil.splitStrLength(upText,spLength);

//                  BufferedImage bufferedImage=QRUtil.encodeLogo(content,logoUrl,true,qrcodeSize);

                    BufferedImage bufferedImage=QRUtil.createImageAndLogoText(content,logoUrl,topText,upText,true,qrcodeSize);

                    //门店编号+门店名称+二维码id
                    ZipEntry entry = new ZipEntry(weixinQrcode.getId()+"."+QRUtil.FORMAT_NAME);
                    zipOutputStream.putNextEntry(entry);
                    ImageIO.write(bufferedImage, QRUtil.FORMAT_NAME, zipOutputStream);
                }
            }
            zipOutputStream.flush();
            zipOutputStream.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public WeixinQrcodeVO getByTicket(String appId, String ticket) {
        return weixinQrcodeMapper.getByTicket(appId,ticket);
    }

}
