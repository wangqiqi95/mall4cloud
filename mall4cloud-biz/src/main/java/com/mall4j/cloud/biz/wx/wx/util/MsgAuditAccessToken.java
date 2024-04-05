package com.mall4j.cloud.biz.wx.wx.util;

import com.google.gson.Gson;
import com.mall4j.cloud.biz.model.chat.SessionTokenModel;
import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会话存档token获取
 */
public class MsgAuditAccessToken {
    private final static Logger logger = LoggerFactory.getLogger(MsgAuditAccessToken.class);
    public static final int MSG_AUDIT_TOKEN_TYPE = 4;

    public static final String BASE_ADDRESS = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    public String getMSGAUDITAccessToken(ConfigService service) {
        Config config = service.getConfig();
        if (null != config) {
            if (config.getSessionLoseTime() > System.currentTimeMillis()) {
                return config.getSessionAgreeToken();
            }
        }
        //config.setCpId("wwf98894bf27379e85");
        //config.setAgentSecret("NIOroyVVW2yJWyRJAS7HjkDkxOlnNqnqSjWeI8qrjEE");
        //String status = requestContactToken(service,config);
        //return status ;
        boolean status = requestContactToken(service,config);
        return status ? getMSGAUDITAccessToken(service) : "";
    }



    private boolean setMSGAUDITAccessToken(Config config, ConfigService server) {
        //return tokenServer.updateInsertToken(queryModel,tokenModel) > 0;
        server.update(config);
        return true;
    }



    private boolean requestContactToken(ConfigService tokenServer,Config config) {
        String url = BASE_ADDRESS + "?" + "corpid=" + config.getSessionCpId() +"&" + "corpsecret=" + config.getSessionSecret();
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(url).get().build()).execute();
            if (response.code() == 200) {
                SessionTokenModel tokenModel = new Gson().fromJson(response.body().string(), SessionTokenModel.class);
                if (tokenModel.getErrcode() == 0) {
                    logger.debug(tokenModel.toString());
                    tokenModel.setLoseTime(System.currentTimeMillis() + tokenModel.getExpires_in() * 1000);
                    tokenModel.setCorpId(config.getCpId());
                    config.setSessionLoseTime(System.currentTimeMillis() + tokenModel.getExpires_in() * 1000);
                    config.setSessionAgreeToken(tokenModel.getAccess_token());
                    setMSGAUDITAccessToken(config,tokenServer);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




}
