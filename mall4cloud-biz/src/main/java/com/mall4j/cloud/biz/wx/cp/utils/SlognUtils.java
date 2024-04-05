package com.mall4j.cloud.biz.wx.cp.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.mall4j.cloud.api.biz.vo.TextHerf;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.util.PlaceholderResolver;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class SlognUtils {

    public static String formatSlogn(String slogn){
        if(CharSequenceUtil.isEmpty(slogn)){
            return null;
        }
        return slogn.replace("${员工姓名}","${staffName}")
                .replace("${客户昵称}","${userName}");
    }

    public static String parseSlogn(String slogn){
        if(CharSequenceUtil.isEmpty(slogn)){
            return null;
        }
        return slogn.replace("${staffName}","${员工姓名}")
                .replace("${userName}","${客户昵称}");
    }

    public static String getSlogan(String staffName, String slogan, String userName){
        log.info("---getSlogan--原始slogan->【{}】  staffVO->【{}】 userName->【{}】",slogan,staffName,userName);
        try {
            PlaceholderResolver defaultResolver = PlaceholderResolver.getDefaultResolver();
            Map<String,Object> keyContent=new LinkedHashMap<>();
            //获取员工信息
            if(defaultResolver.checkVariables(slogan,"${staffName}")){
                keyContent.put("staffName", StrUtil.isNotBlank(staffName)?staffName:"");
            }
            //客户昵称
            if(defaultResolver.checkVariables(slogan,"${userName}")){
                keyContent.put("userName",StrUtil.isNotBlank(userName)?userName:"");
            }
            slogan=defaultResolver.resolveByMap(slogan,keyContent);
        }catch (Exception e){
//            e.printStackTrace();
            log.info("欢迎语变量替换失败 {} {}",e,e.getMessage());
        }

        log.info("---getSlogan--替换后->"+slogan);
        return slogan;
    }

    public static String getTextHerf(String content,String textHerf){
        if(StrUtil.isEmpty(textHerf)){
            return content;
        }
        log.info("---getTextHerf-content:【{}】 textHerf:【{}】",content,textHerf);
        try {
            List<TextHerf> textHerfs= JSONArray.parseArray(textHerf,TextHerf.class);

            PlaceholderResolver defaultResolver = PlaceholderResolver.getDefaultResolver();
            Map<String,Object> keyContent=new LinkedHashMap<>();
            String herf="<a href=\"HREF\">TITLE</a>";
            for (int i = 0; i < textHerfs.size(); i++) {
                String key="${INDEX"+i+"}";
                if(defaultResolver.checkVariables(content,key)){
                    TextHerf textHerfModel=textHerfs.get(i);
                    if(Objects.nonNull(textHerfModel)){
                        herf=herf.replace("TITLE",textHerfModel.getTitle()).replace("HREF",textHerfModel.getHref());
                        keyContent.put("INDEX"+i, herf);
                    }
                }
            }
            content=defaultResolver.resolveByMap(content,keyContent);
        }catch (Exception e){
            log.info("getTextHerf失败 {} {}",e,e.getMessage());
            e.printStackTrace();
        }

        log.info("---getTextHerf--替换后->"+content);
        return content;
    }

    public static void main(String[] strings){
        String content="哈罗欢迎${INDEX0}";
        String textHerf="[{\"title\":\"这是链接\",\"href\":\"https://www.baidu.com/\"}]";
        System.out.println(getTextHerf(content,textHerf));
    }
}
