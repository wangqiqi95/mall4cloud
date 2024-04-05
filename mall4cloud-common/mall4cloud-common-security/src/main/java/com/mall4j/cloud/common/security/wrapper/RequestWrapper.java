package com.mall4j.cloud.common.security.wrapper;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.mall4j.cloud.common.exception.SensitiveException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.filter.SensitiveFilter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Citrus
 * @date 2021/8/13 13:31
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private String body;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrapper(HttpServletRequest request, SensitiveFilter sensitiveFilter) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[1024];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        body = stringBuilder.toString();
        if (StrUtil.isNotBlank(jsonGetValue(body)) && sensitiveFilter.isSensitive(jsonGetValue(body))) {
            throw new SensitiveException(ResponseEnum.SENSITIVE_WORD.getMsg());
        }
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    public static String jsonGetValue(String jsonContent) {
        StringBuilder stringBuilder = new StringBuilder();
        jsonIterator(jsonContent,stringBuilder);
        return stringBuilder.toString();
    }

    private static void jsonIterator(String jsonContent, StringBuilder stringBuilder) {
        //判断传入的是数组还是json
        if (JSONValidator.Type.Array.equals(JSONValidator.from(jsonContent).getType())) {
            JSONArray jsonArray = JSONArray.parseArray(jsonContent);
            for (Object o : jsonArray) {
                jsonIterator(o.toString(), stringBuilder);
            }
        } else if (JSONValidator.Type.Object.equals(JSONValidator.from(jsonContent).getType())) {
            JSONObject jsonObject = JSON.parseObject(jsonContent);
            for (String key : jsonObject.keySet()) {
                Object object = jsonObject.get(key);
                if (Objects.nonNull(object)) {
                    String value = object.toString();
                    if (JSONValidator.Type.Object.equals(JSONValidator.from(value).getType()) || JSONValidator.Type.Array.equals(JSONValidator.from(value).getType())) {
                        jsonIterator(value, stringBuilder);
                    } else {
                        stringBuilder.append(value);
                    }
                }
            }
        }
    }
}
