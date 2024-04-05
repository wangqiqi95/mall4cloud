package com.mall4j.cloud.biz.controller.wx.open;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.mall4j.cloud.api.biz.vo.SessionFileVO;
import com.mall4j.cloud.biz.model.chat.SessionFile;
import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.service.chat.SessionFileService;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.biz.util.TestWxMsgPushUtil;
import com.mall4j.cloud.biz.wx.cp.utils.session.WXBizJsonMsgCrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ua/cp/session")
@Api(tags = "企微会话存档事件")
public class CpSessionArchiveController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private SessionFileService fileService;
    @Autowired
    private TestWxMsgPushUtil testWxMsgPushUtil;

    /**
     * 会话存档：接收消息与事件
     * https://developer.work.weixin.qq.com/document/10514
     */

    @ApiOperation(value = "企微会话存档-开启接收消息")
    @RequestMapping("/message/callback")
    @ResponseBody()
    public void callback(
            @RequestParam(name = "echostr") String echostr,
            @RequestParam(name = "timestamp") String timestamp,
            @RequestParam(name = "nonce") String nonce,
            @RequestParam(name = "msg_signature") String msgSignature,
            HttpServletRequest request, HttpServletResponse response) {

        log.info("会话存档开启接收消息-> echostr: {} timestamp: {} nonce: {} msg_signature: {} ",echostr,timestamp,nonce,msgSignature);

        Config config=configService.getConfig();
        String sToken = config.getSessionToken();
        String sCorpID = config.getSessionCpId();
        String sEncodingAESKey = config.getSessionEncodingAesKey();
        String sEchoStr="error"; //需要返回的明文
        try {
            WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
            sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp,
                    nonce, echostr);
            log.info("会话存档开启接收消息,需要返回的明文: {}",sEchoStr);
            PrintWriter pw = response.getWriter();
            pw.write(sEchoStr);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "企微会话存档-使用接收消息")
    @PostMapping("/message/callback")
    @ResponseBody()
    public void callbackData(HttpServletRequest request,
                             @RequestBody() String sRespData,
                             @RequestParam("msg_signature") String msgSignature,
                             @RequestParam("timestamp") String timestamp,
                             @RequestParam("nonce") String nonce) throws Exception {
        log.info("会话存档开使用接收消息-> sRespData: {} msgSignature: {} timestamp: {} nonce: {} ",sRespData,msgSignature,timestamp,nonce);
        //TODO 推送生产环境
        testWxMsgPushUtil.msgCallback(sRespData,msgSignature,timestamp,nonce);
        Config config=configService.getConfig();
        String sToken = config.getSessionToken();
        String sCorpID = config.getSessionCpId();
        String sEncodingAESKey = config.getSessionEncodingAesKey();
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        String sMsg = wxcpt.DecryptMsg(msgSignature, timestamp, nonce, sRespData);
        log.info("解密decrypt企业微信推送的消息->sMsg：{}",sMsg);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(sMsg);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);
        //获取整个XML消息体，进行解析
        Element root = document.getDocumentElement();
        //返回消息的公共部分，内容，创建时间，消息ID，来源，去处

        NodeList nodelistTime = root.getElementsByTagName("CreateTime");
        String CreateTime = nodelistTime.item(0).getTextContent();
        NodeList nodelistFrom = root.getElementsByTagName("FromUserName");
        String fromUserName = nodelistFrom.item(0).getTextContent();
        NodeList nodelistTo = root.getElementsByTagName("ToUserName");
        String toUserName = nodelistTo.item(0).getTextContent();
        Map<String, String> msgMap = new HashMap<String, String>();
        msgMap.put("CreateTime", CreateTime);

        msgMap.put("fromUser", fromUserName);
        msgMap.put("to", toUserName);
        //消息类型，不同消息返回不同消息体
        NodeList typeNodelist = root.getElementsByTagName("MsgType");
        String MsgType = typeNodelist.item(0).getTextContent();
        msgMap.put("msgType", MsgType);
        //文本直接返回
        if (StringUtils.equals(MsgType, "text")) {
            NodeList nodelistMsgId = root.getElementsByTagName("MsgId");
            String MsgId = nodelistMsgId.item(0).getTextContent();
            msgMap.put("msgId", MsgId);
            NodeList nodelist = root.getElementsByTagName("Content");
            String Content = nodelist.item(0).getTextContent();
            msgMap.put("text", Content);
            //自定义返回消息类型，只支持字符串类型
            Map<String, Object> getMsg = new HashMap<String, Object>();
            getMsg.put("msg", msgMap);
            String message = JSONArray.toJSON(getMsg).toString();

            //图片返回图片需要增加url和mediaId
        }

        long start = new Date().getTime();
        SessionFile file = new SessionFile();
        log.info("调用会话存档记录 =======>start");
        fileService.save(file);
        long end = new Date().getTime();
        log.info("调用会话存档记录 =======>end, 耗时：{}s",((end - start) / 1000.0) );

        /**
         * 延迟处理
         */
//        new Thread(()->{
//            try {
//                long start = new Date().getTime();
//                Thread.sleep(1000*60*3);
//                SessionFile file = new SessionFile();
//                log.info("调用会话存档记录 =======>start");
//                fileService.save(file);
//                long end = new Date().getTime();
//                log.info("调用会话存档记录 =======>end, 耗时：{}s",((end - start) / 1000.0) );
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
    }




}
