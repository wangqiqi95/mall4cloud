package com.mall4j.cloud.common.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.common.bean.SensitiveWord;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.exception.SensitiveException;
import com.mall4j.cloud.common.handler.HttpHandler;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.config.SensitiveWordConfig;
import com.mall4j.cloud.common.security.wrapper.RequestWrapper;
import com.mall4j.cloud.common.security.wrapper.ResponseWrapper;
import com.mall4j.cloud.common.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Citrus
 * @date 2021/8/9 11:12
 */
@Component
public class SensitiveFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    @Autowired
    private HttpHandler httpHandler;
    @Autowired
    private ConfigFeignClient configFeignClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        ServletRequestAttributes attributes = new ServletRequestAttributes(req, resp);
        RequestContextHolder.setRequestAttributes(attributes);
        logger.info("AuthFilter RequestURI :{}", req.getRequestURI().replaceAll("[\r\n]", ""));
        Set<String> sensitiveWhiteSet = SensitiveWordConfig.getSensitiveWhiteSet();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (CollectionUtil.isNotEmpty(sensitiveWhiteSet)) {
            for (String sensitiveUrl : sensitiveWhiteSet) {
                if (antPathMatcher.match(sensitiveUrl, req.getRequestURI())) {
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }
        ResponseWrapper responseWrapper = new ResponseWrapper(resp);
        // 敏感词输入过滤
        if (StrUtil.isNotBlank(req.getContentType()) && req.getContentType().contains(ContentType.JSON.getValue())) {
            RequestWrapper requestWrapper;
            try {
                requestWrapper = new RequestWrapper(req, this);
            } catch (SensitiveException sensitiveException) {
                httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.SENSITIVE_WORD));
                return;
            }
            //xss
            chain.doFilter(requestWrapper, responseWrapper);
        } else {
            Map<String, String[]> parameterMap = req.getParameterMap();
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : parameterMap.keySet()) {
                stringBuilder.append(parameterMap.get(s)[0]);
            }
            // 传入参数是否包含敏感词,最小匹配
            if (StrUtil.isNotBlank(stringBuilder.toString()) && isSensitive(stringBuilder.toString())) {
                httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.SENSITIVE_WORD));
                return;
            }
            chain.doFilter(req, responseWrapper);
        }
        if (Objects.nonNull(responseWrapper.getContentType()) && responseWrapper.getContentType().contains(ContentType.JSON.getValue())) {
            byte[] content = responseWrapper.getContent();
            //敏感词输出过滤
            if (content.length > 0) {
                String outPutString = new String(content, StandardCharsets.UTF_8);
                //属于json格式再处理
                String sensitiveText = null;
                try {
                    sensitiveText = replaceSensitiveWord(outPutString, "*");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ServletOutputStream outputStream = resp.getOutputStream();
                outputStream.write(sensitiveText.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        }
    }

    /**
     * 缓存
     */
    private static final Cache<Object, Object> CONFIG_MAP = Caffeine.newBuilder()
            // 数量上限
            .maximumSize(1)
            // 过期机制
            .expireAfterWrite(5, TimeUnit.MINUTES).build();
    /**
     * 最小匹配
     */
    public static int minMatchType = 1;
    /**
     * 最大匹配
     */
    public static int maxMatchType = 2;

    private static final String IS_END = "isEnd";
    private static final String IS_ONE = "1";

    /**
     * 是否有敏感词
     *
     * @param txt
     * @return
     */
    public boolean isSensitive(String txt) {
        return isSensitive(txt, minMatchType);
    }

    public boolean isSensitive(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = this.checkSensitiveWord(txt, i, matchType);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 替换敏感词,value
     *
     * @param txt
     * @param replaceChar
     * @return
     */
    public String replaceSensitiveWord(String txt, String replaceChar) {
        JSON json;
        if (JSONValidator.Type.Object.equals(JSONValidator.from(txt).getType())) {
            json = JSON.parseObject(txt);
        } else if (JSONValidator.Type.Array.equals(JSONValidator.from(txt).getType())) {
            json = JSON.parseArray(txt);
        } else {
            return replaceSensitiveWord(txt, maxMatchType, replaceChar);
        }
        jsonIterator(json, replaceChar);
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(json);
        } catch (IOException e) {
            throw new LuckException("io 异常");
        }
        return jsonStr;
    }

    private String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        Set<String> sensitiveWordSet = this.getSensitiveWord(txt, matchType);
        Iterator<String> iterator = sensitiveWordSet.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        return resultTxt;
    }

    private void jsonIterator(JSON json, String replaceChar) {
        if (json instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) json;
            for (String key : jsonObject.keySet()) {
                Object object = jsonObject.get(key);
                if (Objects.nonNull(object)) {
                    String value = object.toString();
                    if (object instanceof JSON) {
                        jsonIterator((JSON) object, replaceChar);
                    } else if (object instanceof String) {
                        jsonObject.put(key, replaceSensitiveWord(value, maxMatchType, replaceChar));
                    }
                }
            }
        } else if (json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            for (int key = 0; key < jsonArray.size(); key++) {
                Object object = jsonArray.get(key);
                if (object instanceof JSON) {
                    jsonIterator((JSON) object, replaceChar);
                } else if (object instanceof String) {
                    jsonArray.set(key, replaceSensitiveWord(object.toString(), maxMatchType, replaceChar));
                }
            }
        }
    }

    public Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, maxMatchType);
    }

    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }

    private String getReplaceChars(String replaceChar, int length) {
        StringBuilder resultReplace = new StringBuilder();
        resultReplace.append(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace.append(replaceChar);
        }
        return resultReplace.toString();
    }

    private int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        Map nowMap = getSensitiveWordMap();
        boolean flag = false;
        char word = 0;
        int matchFlag = 0;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (Objects.isNull(nowMap)) {
                break;
            }
            matchFlag++;
            if (isEnd(nowMap)) {
                flag = true;
                if (Objects.equals(SensitiveFilter.minMatchType, matchType)) {
                    break;
                }
            }
        }
        if (matchFlag <= 1 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }


    private boolean isEnd(Map nowMap) {
        boolean flag = false;
        if (Objects.equals(nowMap.get(IS_END), IS_ONE)) {
            flag = true;
        }
        return flag;
    }


    private Map getSensitiveWordMap() {
        Map finalMap = (Map) CONFIG_MAP.get(Constant.SENSITIVE_WORDS, cacheKey -> {
            SensitiveWord sensitiveWord;
            try {
                ServerResponseEntity<String> configResponse = configFeignClient.getConfig(Constant.SENSITIVE_WORDS);
                if (!configResponse.isSuccess()) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
                String value = configResponse.getData();
                sensitiveWord = Json.parseObject(value, SensitiveWord.class);
            } catch (Exception e) {
                return Collections.emptyMap();
            }
            if (StrUtil.isBlank(sensitiveWord.getSensitiveWords())) {
                return Collections.emptyMap();
            }
            //根据中文逗号分隔
            List<String> sensitiveWordsList = Arrays.asList(sensitiveWord.getSensitiveWords().split(Constant.CN_COMMA));
            HashSet<String> sensitiveWordSet = new HashSet<>(sensitiveWordsList);
            Iterator<String> iterator = sensitiveWordSet.iterator();
            String key;
            Map nowMap;
            Map<String, String> newWordMap;
            Map<Object, Object> sensitiveWordMap = new HashMap<>(sensitiveWordSet.size());
            while (iterator.hasNext()) {
                //敏感词
                key = iterator.next();
                nowMap = sensitiveWordMap;
                for (int i = 0; i < key.length(); i++) {
                    //敏
                    char charAt = key.charAt(i);
                    Object wordMap = nowMap.get(charAt);
                    if (wordMap != null) {
                        //逐字加入map
                        nowMap = (Map) wordMap;
                    } else {
                        newWordMap = new HashMap<>(sensitiveWordSet.size());
                        //最后一个字，加标记
                        newWordMap.put("isEnd", "0");
                        nowMap.put(charAt, newWordMap);
                        nowMap = newWordMap;
                    }
                    if (i == key.length() - 1) {
                        //结束
                        nowMap.put("isEnd", "1");
                    }
                }
            }
            return sensitiveWordMap;
        });

        if (finalMap == null) {
            CONFIG_MAP.invalidate(Constant.SENSITIVE_WORDS);
        }

        return finalMap;
    }
}
