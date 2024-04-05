package com.mall4j.cloud.biz.controller;

import com.mall4j.cloud.api.biz.dto.cp.externalcontact.CpChatAddMsgTemplateDTO;
import com.mall4j.cloud.api.biz.feign.WxCpAuth2FeignClient;
import com.mall4j.cloud.api.biz.vo.WxCpUserInfoVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.wx.externalcontact.ExtendWxCpMsgTemplate;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.manager.WeixinCpMediaManager;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Image;
import me.chanjar.weixin.cp.bean.external.msg.Link;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ua/test")
public class CpExternalTestController {

    @Autowired
    private WeixinCpExternalManager weixinCpExternalManager;

    @Autowired
    private WeixinCpMediaManager weixinCpMediaManager;

    @Autowired
    private WxCpAuth2FeignClient wxCpAuth2FeignClient;


    @ApiOperation(value = "创建企业群发-发送给客户群", notes = "创建企业群发-发送给客户群")
    @PostMapping("/addChatMsgTemplate")
    public ServerResponseEntity<WxCpMsgTemplateAddResult> addChatMsgTemplate() throws WxErrorException {
        Attachment imageAttachment = new Attachment();
        String testImage = "https://xcx-sit-uat.oss-cn-shanghai.aliyuncs.com/2023/01/03/8385e4476bb946d6b668ffaef6c65756.jpg";
        Image image = new Image();
        image.setPicUrl(testImage);
        imageAttachment.setMsgType("image");
        imageAttachment.setImage(image);

        CpChatAddMsgTemplateDTO extendWxCpMsgTemplate = new CpChatAddMsgTemplateDTO();

//        List<Attachment> attachments = Arrays.asList(imageAttachment);
//        extendWxCpMsgTemplate.setAttachments(attachments);

        extendWxCpMsgTemplate.setChatType("group");
        List<String> chatIdList = Arrays.asList("wrRqXCCgAAXhTGuvNIvSYTmNFc0Ew9nw");

        extendWxCpMsgTemplate.setSender("HaiXiang");

        extendWxCpMsgTemplate.setChatIdList(chatIdList);
        Text text = new Text();
        text.setContent("就想问一下我帅不");
        extendWxCpMsgTemplate.setText(text);
        return weixinCpExternalManager.addChatMsgTemplate(extendWxCpMsgTemplate);
    }


    @ApiOperation(value = "创建企业群发-发送给客户", notes = "创建企业群发-发送给客户")
    @PostMapping("/addMsgTemplate")
    public void addMsgTemplate() throws WxErrorException {

        Attachment imageAttachment = new Attachment();
        String testImage = "https://xcx-sit-uat.oss-cn-shanghai.aliyuncs.com/2023/01/03/8385e4476bb946d6b668ffaef6c65756.jpg";
        Image image = new Image();
        image.setPicUrl(testImage);
        imageAttachment.setMsgType("image");
        imageAttachment.setImage(image);

        Attachment linkAttachment = new Attachment();
        Link link = new Link();
        link.setPicUrl(testImage);
        link.setTitle("就一个标题");
        link.setDesc("就一个描述");
        link.setUrl("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E6%88%91%E8%B6%85%E5%B8%85&step_word=&hs=0&pn=3&spn=0&di=7189064908862914561&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=1580276315%2C1703776145&os=1836074558%2C3948278956&simid=1580276315%2C1703776145&adpicid=0&lpn=0&ln=1508&fr=&fmq=1678949816559_R&fm=result&ic=&s=undefined&hd=&latest=&copyright=&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fwx3.sinaimg.cn%2Flarge%2Fa6a681ebgy1gpfy0gnbcpj20b40b4weq.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fktw5qtg2dnn_z%26e3Bv54AzdH3F2tuAzdH3Fln8b9b8_z%26e3Bip4s&gsm=1e&rpstart=0&rpnum=0&islist=&querylist=&nojc=undefined&dyTabStr=MCw0LDEsNiw1LDMsMiw3LDgsOQ%3D%3D");
        linkAttachment.setMsgType("link");
        linkAttachment.setLink(link);

        List<Attachment> attachments = Arrays.asList(imageAttachment, linkAttachment);

        ExtendWxCpMsgTemplate extendWxCpMsgTemplate = new ExtendWxCpMsgTemplate();

//        extendWxCpMsgTemplate.setAttachments(attachments);
        extendWxCpMsgTemplate.setChatType("single");
        List<String> externalUseridList = Arrays.asList("wrRqXCCgAAXhTGuvNIvSYTmNFc0Ew9nw");
        extendWxCpMsgTemplate.setExternalUserid(externalUseridList);
        extendWxCpMsgTemplate.setSender("eury");
        Text text = new Text();
        text.setContent("就想问一下我帅不");
        extendWxCpMsgTemplate.setText(text);
        WxCpService wxCpService = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
        System.out.println(wxCpService.getAccessToken());
        WxCpMsgTemplateAddResult wxCpMsgTemplateAddResult = wxCpService
                .getExternalContactService()
                .addMsgTemplate(extendWxCpMsgTemplate);
        //群发任务完成情况结果集 msgid（群发任务ID）、userid（发送人ID）、limit（条数）、cursor（游标）
//        WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getExternalContactService().getGroupMsgSendResult();
        wxCpMsgTemplateAddResult.toJson();
    }

    @PostMapping("/testCancel")
    public void testCancel() throws Exception {
        weixinCpExternalManager.cancelGroupmsgSend("msgRG2WBgAAhG_DeLjzBbrzW0Hb3MjXZA");
    }

    @GetMapping("/getWxUserInfo")
    @ApiOperation(value = "获取企微员工信息", notes = "获取企微员工信息")
    public ServerResponseEntity<WxCpUserInfoVO> getWxUserInfo(String userid)  {
        return wxCpAuth2FeignClient.getUserInfoById(userid);
    }




    @PostMapping("/testUpload")
    public ServerResponseEntity testUpload(String path) throws Exception {

//        String path = "https://imgsa.baidu.com/forum/pic/item/1878c124b899a901e1042c751d950a7b0208f50c.jpg";

        WxMediaUploadResult wxMediaUploadResult = weixinCpMediaManager.uploadByUrlFile(path, "image", true);
        System.out.println(wxMediaUploadResult.getMediaId());
        return ServerResponseEntity.success(wxMediaUploadResult.getMediaId());
    }


    public static void main(String[] args) throws IOException {

        String urlPath = "https://imgsa.baidu.com/forum/pic/item/1878c124b899a901e1042c751d950a7b0208f50c.jpg";

        URL urlfile = new URL(urlPath);

        InputStream inputStream = urlfile.openStream();

        BufferedImage image = ImageIO.read(inputStream);

        int height = image.getHeight();

        int width = image.getWidth();


        System.out.printf("This height is %s , This width is %s",height,width);






        Canvas canvas = new Canvas();
//        canvas.createImage()
    }




}
