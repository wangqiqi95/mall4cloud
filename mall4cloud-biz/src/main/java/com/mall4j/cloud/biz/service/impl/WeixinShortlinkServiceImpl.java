package com.mall4j.cloud.biz.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.constant.SceneType;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.mapper.WeixinShortlinkItemMapper;
import com.mall4j.cloud.biz.model.WeixinShortlinkItem;
import com.mall4j.cloud.biz.service.QrcodeTicketService;
import com.mall4j.cloud.biz.vo.WeixinQrcodeTentacleStoreItemVO;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordItemVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkRecordVo;
import com.mall4j.cloud.biz.vo.WeixinShortlinkVO;
import com.mall4j.cloud.biz.model.WeixinShortlink;
import com.mall4j.cloud.biz.mapper.WeixinShortlinkMapper;
import com.mall4j.cloud.biz.service.WeixinShortlinkService;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.util.ShortUrlGenerator;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mall4j.cloud.common.util.SpringContextUtils.applicationContext;

/**
 *
 *
 * @author gmq
 * @date 2022-06-09 14:31:51
 */
@Service
@Slf4j
public class WeixinShortlinkServiceImpl extends ServiceImpl<WeixinShortlinkMapper, WeixinShortlink> implements WeixinShortlinkService {

    @Autowired
    private WeixinShortlinkMapper weixinShortlinkMapper;
    @Autowired
    private WeixinShortlinkItemMapper weixinShortlinkItemMapper;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;
    @Autowired
    private FeignShopConfig feignShopConfig;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private QrcodeTicketService qrcodeTicketService;
    @Autowired
    private WxConfig wxConfig;

    @Override
    public ServerResponseEntity<String> saveTo(WeixinShortlink weixinShortlink) {
        Long userId = AuthUserContext.get().getUserId();
        Date now = new Date();

        String domain=feignShopConfig.getMaShortlinkDomin();
        String shortkey=ShortUrlGenerator.shortkey(weixinShortlink.getPagePath());
        String shortUrl = domain + shortkey;

        String[] urlStr = weixinShortlink.getPagePath().split("\\?");
        String path = urlStr[0];
        weixinShortlink.setPagePath(path);
        if(urlStr.length>1){
            String scen = urlStr[1];
            weixinShortlink.setScen(scen);
        }

        weixinShortlink.setShortKey(shortkey);
        weixinShortlink.setDoMain(domain);
        weixinShortlink.setOpenNumber(0);
        weixinShortlink.setOpenPeople(0);
        weixinShortlink.setShortUrl(shortUrl);
        weixinShortlink.setAppId(feignShopConfig.getWxMiniApp().getAppId());
        weixinShortlink.setCreateBy(AuthUserContext.get().getUsername());
        weixinShortlink.setCreateTime(now);

        this.save(weixinShortlink);


        return ServerResponseEntity.success(shortUrl);

    }

    @Override
    public WeixinShortlinkVO getShortkey(String shortkey) {
        LambdaQueryWrapper<WeixinShortlink> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WeixinShortlink::getShortKey,shortkey);
        lambdaQueryWrapper.eq(WeixinShortlink::getAppId,feignShopConfig.getWxMiniApp().getAppId());
        lambdaQueryWrapper.eq(WeixinShortlink::getDelFlag,0);
        WeixinShortlink weixinShortlink=this.getOne(lambdaQueryWrapper,false);
        if(Objects.isNull(weixinShortlink)){
            throw new LuckException("短链获取失败");
        }
        WeixinShortlinkVO weixinShortlinkVO=mapperFacade.map(weixinShortlink,WeixinShortlinkVO.class);
        //生产短链标识【短链ID、短链场景】skxLinkId：短链/触点ID；skxScene：应用场景0代表：短链；1代表：触点
        String shortLinkSign = "&skxLinkId="+weixinShortlink.getId()+"&skxScene="+ SceneType.SHORT_LINK;
        String linkSign = "skxLinkId="+weixinShortlink.getId()+"&skxScene="+ SceneType.SHORT_LINK;
        //生成小程序短链
        if(StrUtil.isNotBlank(weixinShortlinkVO.getPagePath())){
            ServerResponseEntity<String> response=qrcodeTicketService.generateUrlLink(weixinShortlinkVO.getPagePath(),
                    false,
                    StringUtils.isNotEmpty(weixinShortlinkVO.getScen()) ? weixinShortlinkVO.getScen().concat(shortLinkSign) : linkSign
                    ,null);
            if(response.isSuccess() && StrUtil.isNotBlank(response.getData())){
                weixinShortlinkVO.setShortLink(response.getData());
            }else{
                throw new LuckException("短链获取失败");
            }
        }else{
            throw new LuckException("短链获取失败，小程序路径为空");
        }

        return weixinShortlinkVO;
    }

    /**
     * 查询短链列表
     * @param weixinShortlinkRecordDTO     查询短链列表传参
     * @return
     */
    /*@Override
    public List<WeixinShortlinkRecordVo> selectShortLinkRecordList(WeixinShortlinkRecordDTO weixinShortlinkRecordDTO) {
        return weixinShortlinkMapper.selectShortLinkRecordList(weixinShortlinkRecordDTO);
    }*/
    @Override
    public PageVO<WeixinShortlinkRecordVo> selectShortLinkRecordList(WeixinShortlinkRecordDTO weixinShortlinkRecordDTO) {
        PageVO<WeixinShortlinkRecordVo> pageVO = PageUtil.doPage(weixinShortlinkRecordDTO, () -> weixinShortlinkMapper.selectShortLinkRecordList(weixinShortlinkRecordDTO));
        if(CollectionUtil.isNotEmpty(pageVO.getList())){
            pageVO.getList().forEach(shortLink -> {
                if(StringUtils.isNotEmpty(shortLink.getScene())){
                    StringBuffer tentacleSign = new StringBuffer();
                    tentacleSign.append(shortLink.getOriginalLink());
                    tentacleSign.append("?");
                    tentacleSign.append(shortLink.getScene());
                    shortLink.setOriginalLink(String.valueOf(tentacleSign));
                }
            });
        }
        return pageVO;
    }

    /**
     * 查询短链列表详情
     * @param weixinShortlinkRecordItemDTO 查询短链列表详情参数
     * @return
     */
    @Override
    public PageVO<WeixinShortlinkRecordItemVo> selectShortLinkRecordItemList(WeixinShortlinkRecordItemDTO weixinShortlinkRecordItemDTO) {
        PageVO<WeixinShortlinkRecordItemVo> pageVO = PageUtil.doPage(weixinShortlinkRecordItemDTO, () -> weixinShortlinkMapper.selectShortLinkRecordItemList(weixinShortlinkRecordItemDTO));
        pageVO.getList().stream().peek(item ->{
            UserApiVO data = userFeignClient.getUserByUnionId(item.getUniId()).getData();
            if(ObjectUtils.isNotEmpty(data)){
                item.setStaffStoreId(data.getServiceStoreId());
                StoreVO storeVO = storeFeignClient.findByStoreId(data.getServiceStoreId());
                if(storeVO != null){
                    item.setStaffStoreName(storeVO.getName());
                    item.setStaffStoreCode(storeVO.getStoreCode());
                }
            }
        }).collect(Collectors.toList());
        return pageVO;
    }

    /**
     * 查询短链列表详情分页
     * @param weixinShortlinkRecordItemPageDTO 查询短链列表详情参数
     * @return
     */
    @Override
    public PageVO<WeixinShortlinkRecordItemVo> selectShortLinkRecordItemPage(WeixinShortlinkRecordItemPageDTO weixinShortlinkRecordItemPageDTO) {
        PageVO<WeixinShortlinkRecordItemVo> pageVO = PageUtil.doPage(weixinShortlinkRecordItemPageDTO, () -> weixinShortlinkMapper.selectShortLinkRecordItemPage(weixinShortlinkRecordItemPageDTO));
        pageVO.getList().stream().peek(item ->{
            UserApiVO data = userFeignClient.getUserByUnionId(item.getUniId()).getData();
            if(ObjectUtils.isNotEmpty(data)){
                item.setStaffStoreId(data.getServiceStoreId());
                StoreVO storeVO = storeFeignClient.findByStoreId(data.getServiceStoreId());
                if(storeVO != null){
                    item.setStaffStoreName(storeVO.getName());
                    item.setStaffStoreCode(storeVO.getStoreCode());
                }
            }
        }).collect(Collectors.toList());
        return pageVO;
    }

    /**
     * 短链列表详情导出
     * @param param 短链列表详情导出参数
     * @return
     */
    @Override
    public String shortLinkRecordItemExcel(WeixinShortlinkRecordItemPageDTO param) {
        try {
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(ShortLinkRecordItemExport.SHORT_LINK_RECORD_ITEM_DETAIL_FILE_NAME);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity serverResponseEntity = downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downloadCenterId = null;
            if(serverResponseEntity.isSuccess()){
                downloadCenterId=Long.parseLong(serverResponseEntity.getData().toString());
            }

            ShortLinkRecordItemExport shortLinkRecordItemExport = new ShortLinkRecordItemExport();
            shortLinkRecordItemExport.setWeixinShortlinkRecordItemPageDTO(param);
            shortLinkRecordItemExport.setDownloadCenterId(downloadCenterId);
            applicationContext.publishEvent(shortLinkRecordItemExport);

            return "操作成功，请转至下载中心下载";
        }catch (Exception e){
            log.error("短链列表详情导出错误: " + e.getMessage(), e);
            return "操作失败";
        }
    }

    /**
     * 新增短链详情记录
     * @param shortLinkRecordId    短链记录表ID
     * @return
     */
    @Override
    public ServerResponseEntity<String> saveShortLinkRecordItem(String shortLinkRecordId,String code) throws WxErrorException {
        //UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        int num = 0;

        log.info("新增短链详情记录 微信Code【{}】",code);
        code = code.replace("\"", "");
        WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(code);
        log.info("小程序登录，获取用户session【{}】",session != null ? JSON.toJSONString(session):"未获取到session信息");

        WeixinShortlinkItem weixinShortlinkItem = new WeixinShortlinkItem();
        String unionId = session.getUnionid();
        UserApiVO user = userFeignClient.getUserByUnionId(unionId).getData();
        log.info("获取到的用户消息为：{}", Json.toJsonString(user));
        weixinShortlinkItem.setShortlinkRecordId(Long.valueOf(shortLinkRecordId));
        weixinShortlinkItem.setCheckTime(new Date());
        weixinShortlinkItem.setUniId(unionId);
        if(Objects.nonNull(user)){
            weixinShortlinkItem.setNickName(user.getNickName());
            weixinShortlinkItem.setVipCode(user.getVipcode());
        }

        // 当详情表新增数据成功之后，短链记录表中的点击人数和次数要进行递增
        num = weixinShortlinkMapper.selectShortLinkItemOfUserId(unionId, shortLinkRecordId);
        log.info("查询会员短链记录条数为：{}", num);

        int result = weixinShortlinkItemMapper.insert(weixinShortlinkItem);
        if (result == 0) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }

        WeixinShortlink weixinShortlink = weixinShortlinkMapper.selectById(shortLinkRecordId);
        if(weixinShortlink == null){
            return ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID, "数据异常，并未找到对应的短链记录");
        }
        weixinShortlink.setOpenNumber(weixinShortlink.getOpenNumber() + 1);
        if(num < 1){
            weixinShortlink.setOpenPeople(weixinShortlink.getOpenPeople() + 1);
        }
        weixinShortlinkMapper.updateById(weixinShortlink);
        return ServerResponseEntity.success("短链详情记录新增成功");
    }

}
