package com.mall4j.cloud.common.security.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 返回值输出代理类
 * @author Citrus
 * @date 2021/8/11 14:09
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream buffer;

    private final ServletOutputStream outputStream;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        buffer = new ByteArrayOutputStream();
        outputStream = new WrapperOutPutStream(buffer);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (Objects.nonNull(outputStream)) {
            outputStream.flush();
        }
    }

    public byte[] getContent() throws IOException{
        flushBuffer();
        return buffer.toByteArray();
    }

    static class WrapperOutPutStream extends ServletOutputStream {

        private final ByteArrayOutputStream bos;

        public WrapperOutPutStream(ByteArrayOutputStream bos) {
            this.bos = bos;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }
    }
}
