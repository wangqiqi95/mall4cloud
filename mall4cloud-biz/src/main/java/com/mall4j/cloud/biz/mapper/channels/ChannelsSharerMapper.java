package com.mall4j.cloud.biz.mapper.channels;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.biz.dto.ChannelsSharerDto;
import com.mall4j.cloud.biz.dto.channels.sharer.SharerPageListReqDto;
import com.mall4j.cloud.biz.dto.channels.sharer.SharerQrCodeImgListReqDto;
import com.mall4j.cloud.biz.model.channels.LiveStoreSharer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 视频号分享员
 * @Author axin
 * @Date 2023-02-23 13:58
 **/
@Mapper
public interface ChannelsSharerMapper extends BaseMapper<LiveStoreSharer> {
    /**
     *
     * @param reqDto
     * @return
     */
    List<LiveStoreSharer> queryList(@Param("reqDto") SharerPageListReqDto reqDto);

    /**
     * 查询未失效二维码
     * @param reqDto
     * @return
     */
    List<LiveStoreSharer> queryQrCodeImgList(@Param("reqDto") SharerQrCodeImgListReqDto reqDto);

    List<ChannelsSharerDto> getByOpenIds(@Param("openids") List<String> openids);

    void batchInsert(@Param("reqDtos") List<LiveStoreSharer> reqDtos);
}
