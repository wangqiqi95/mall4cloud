package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.api.biz.dto.channels.response.EcUploadResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @date 2023/3/17
 */
public interface ChannelsFileService {
    void getMedia(String mediaId, HttpServletResponse response);

    EcUploadResponse uploadMedia(String url);
}
