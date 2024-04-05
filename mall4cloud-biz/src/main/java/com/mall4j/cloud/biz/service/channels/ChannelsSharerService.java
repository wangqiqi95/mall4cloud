package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.api.biz.dto.ChannelsSharerDto;
import com.mall4j.cloud.biz.dto.channels.sharer.*;
import com.mall4j.cloud.biz.model.channels.LiveStoreSharer;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import springfox.documentation.service.OpenIdConnectScheme;

import java.util.List;

/**
 * @Description 视频号分享员
 * @Author axin
 * @Date 2023-02-23 13:54
 **/

public interface ChannelsSharerService {

    /**
     * 绑定视频号分享员
     * @param reqDto
     */
    void bind(SharerBindReqDto reqDto);

    /**
     * 批量绑定员工
     * @param reqDtos
     */
    void batchBind(List<SharerBindReqDto> reqDtos);

    /**
     * 重新绑定分享员
     * @param reqDto
     */
    void rebind(SharerReBindReqDto reqDto);

    /**
     * 取消绑定分享员
     * @param reqDto
     */
    void unbind(SharerUnBindReqDto reqDto);

    /**
     * 分享员列表
     * @param reqDto
     * @return
     */
    PageVO<SharerPageListRespDto> pageList(SharerPageListReqDto reqDto);

    /**
     * 获取绑定二维码Base64
     * @param id
     * @return
     */
    String getQrImgBase64(Long id);


    /**
     * 同步绑定的分享员信息
     */
    void syncSuccBind();

    /**
     * 导出未失效二维码-未绑定
     * @param reqDto
     */
    ServerResponseEntity<String> exportQRCode(SharerQrCodeImgListReqDto reqDto);

    Long getSharerStaffIdByOpenId(String openid);

    List<ChannelsSharerDto> getByOpenIds(List<String> openids);

    /**
     * 创建分享员绑定任务
     */
    void syncBind(LiveStoreSharer liveStoreSharer);

}
