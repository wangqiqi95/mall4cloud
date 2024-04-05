package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.constant.channels.LiveStoreSharerBindStatus;
import com.mall4j.cloud.api.biz.constant.channels.ShareType;
import com.mall4j.cloud.api.biz.dto.ChannelsSharerDto;
import com.mall4j.cloud.api.biz.dto.channels.request.sharer.SearchSharerReq;
import com.mall4j.cloud.api.biz.dto.channels.request.sharer.SharerBindReq;
import com.mall4j.cloud.api.biz.dto.channels.request.sharer.SharerUnbindReq;
import com.mall4j.cloud.api.biz.dto.channels.response.sharer.SearchSharerResp;
import com.mall4j.cloud.api.biz.dto.channels.response.sharer.SharerBindResp;
import com.mall4j.cloud.api.biz.dto.channels.response.sharer.SharerUnbindResp;
import com.mall4j.cloud.api.platform.dto.CalcingDownloadRecordDTO;
import com.mall4j.cloud.api.platform.feign.DownloadCenterFeignClient;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.wx.wx.channels.SharerApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.sharer.*;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSharerMapper;
import com.mall4j.cloud.biz.model.channels.LiveStoreSharer;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description 视频号分享员
 * @Author axin
 * @Date 2023-02-23 13:55
 **/
@Slf4j
@Service
public class ChannelsSharerServiceImpl implements ChannelsSharerService {

    @Autowired
    private SharerApi sharerApi;
    @Autowired
    private StaffFeignClient staffFeignClient;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private ChannelsSharerMapper channelsSharerMapper;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DownloadCenterFeignClient downloadCenterFeignClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bind(SharerBindReqDto reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到用户登录信息");
        }
        ServerResponseEntity<StaffVO> staffVOServerResponseEntity = staffFeignClient.getStaffById(reqDto.getStaffId());
        StaffVO staffVO = staffVOServerResponseEntity.getData();
        if(staffVOServerResponseEntity.isFail() || Objects.isNull(staffVO)){
            throw new LuckException("未找到员工");
        }
        if(StringUtils.isBlank(staffVO.getWeChatNo())){
            throw new LuckException("员工未绑定微信号,请先进行微信号绑定");
        }

        LiveStoreSharer liveStoreSharer = channelsSharerMapper.selectOne(Wrappers.lambdaQuery(LiveStoreSharer.class)
                .eq(Objects.nonNull(reqDto.getStaffId()), LiveStoreSharer::getStaffId, staffVO.getId())
                .eq(Objects.nonNull(reqDto.getId()), LiveStoreSharer::getId, reqDto.getId()));

        SharerBindReq sharerBindReq = new SharerBindReq();
        sharerBindReq.setUsername(staffVOServerResponseEntity.getData().getWeChatNo());
        log.info("绑定分享员入参:{}", JSON.toJSONString(sharerBindReq));
        SharerBindResp sharerBindResp = sharerApi.bind(wxConfig.getWxEcToken(), sharerBindReq);
        log.info("绑定分享员出参:{}",JSON.toJSONString(sharerBindResp));
        if(sharerBindResp.getErrcode() != 0){
            throw new LuckException(sharerBindResp.getErrmsg());
        }

        Date date = new Date();
        if(liveStoreSharer == null) {
            liveStoreSharer = new LiveStoreSharer();
            liveStoreSharer.setQrcodeImgExpireTime(DateUtil.offsetDay(date, 3));
            liveStoreSharer.setCreatePerson(userInfoInTokenBO.getUserId());
            liveStoreSharer.setUpdatePerson(userInfoInTokenBO.getUserId());
            liveStoreSharer.setName(staffVO.getStaffName());
            liveStoreSharer.setSharerType(ShareType.EVERYMAN.getValue());
            liveStoreSharer.setQrcodeImgCreateTime(date);
            liveStoreSharer.setStaffId(staffVO.getId());
            liveStoreSharer.setStaffNo(staffVO.getStaffNo());
            liveStoreSharer.setQrcodeImg(Base64Utils.decodeFromString(sharerBindResp.getQrcodeImgBase64()));
            liveStoreSharer.setQrcodeImgBase64(sharerBindResp.getQrcodeImgBase64());
            liveStoreSharer.setCreateTime(date);
            liveStoreSharer.setUpdateTime(date);
            liveStoreSharer.setBindStatus(LiveStoreSharerBindStatus.BIND_WAIT.getValue());
            channelsSharerMapper.insert(liveStoreSharer);
        }else if(Objects.nonNull(reqDto.getId())){
            LambdaUpdateWrapper<LiveStoreSharer> wrappers = Wrappers.lambdaUpdate(liveStoreSharer)
                            .set(LiveStoreSharer::getUpdatePerson,userInfoInTokenBO.getUserId())
                            .set(LiveStoreSharer::getQrcodeImgCreateTime,date)
                            .set(LiveStoreSharer::getQrcodeImgExpireTime,DateUtil.offsetDay(date, 3))
                            .set(LiveStoreSharer::getQrcodeImg,Base64Utils.decodeFromString(sharerBindResp.getQrcodeImgBase64()))
                            .set(LiveStoreSharer::getQrcodeImgBase64,sharerBindResp.getQrcodeImgBase64())
                            .set(LiveStoreSharer::getUpdateTime,date)
                            .set(LiveStoreSharer::getOpenid,null)
                            .set(LiveStoreSharer::getBindTime,null)
                            .set(LiveStoreSharer::getUnbindTime,null)
                            .set(LiveStoreSharer::getNickname,null)
                            .set(LiveStoreSharer::getBindStatus,LiveStoreSharerBindStatus.BIND_WAIT.getValue())
                            .eq(LiveStoreSharer::getId,liveStoreSharer.getId());
            channelsSharerMapper.update(liveStoreSharer,wrappers);
        }else {
            throw new LuckException("已添加记录");
        }
    }

    @Override
    @Transactional
    public void batchBind(List<SharerBindReqDto> reqDtos) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到用户登录信息");
        }

        if(CollectionUtils.isNotEmpty(reqDtos)){
            List<Long> staffIds = reqDtos.stream().map(SharerBindReqDto::getStaffId).collect(Collectors.toList());

            List<LiveStoreSharer> liveStoreSharers = channelsSharerMapper.selectList(Wrappers.lambdaQuery(LiveStoreSharer.class)
                    .in(LiveStoreSharer::getStaffId, staffIds));
            if(CollectionUtils.isNotEmpty(liveStoreSharers)){
                Set<Long> existStaffIds = liveStoreSharers.stream().map(LiveStoreSharer::getStaffId).collect(Collectors.toSet());
                throw new LuckException("该员工已添加记录:"+StringUtils.join(existStaffIds));
            }

            List<LiveStoreSharer> insertList = Lists.newArrayList();
            Date date = new Date();
            reqDtos.forEach(reqDto->{
                ServerResponseEntity<StaffVO> staffVOServerResponseEntity = staffFeignClient.getStaffById(reqDto.getStaffId());
                StaffVO staffVO = staffVOServerResponseEntity.getData();
                if(staffVOServerResponseEntity.isFail() || Objects.isNull(staffVO)){
                    throw new LuckException("未找到员工"+reqDto.getStaffId());
                }
                if(StringUtils.isBlank(staffVO.getWeChatNo())){
                    throw new LuckException(reqDto.getStaffId()+"员工未绑定微信号,请先进行微信号绑定");
                }
                LiveStoreSharer liveStoreSharer = new LiveStoreSharer();
                liveStoreSharer.setCreatePerson(userInfoInTokenBO.getUserId());
                liveStoreSharer.setUpdatePerson(userInfoInTokenBO.getUserId());
                liveStoreSharer.setName(staffVO.getStaffName());
                liveStoreSharer.setSharerType(ShareType.EVERYMAN.getValue());
                liveStoreSharer.setQrcodeImgCreateTime(date);
                liveStoreSharer.setQrcodeImgExpireTime(DateUtil.offsetDay(date, 3));
                liveStoreSharer.setStaffId(staffVO.getId());
                liveStoreSharer.setStaffNo(staffVO.getStaffNo());
                liveStoreSharer.setBindStatus(LiveStoreSharerBindStatus.INIT.getValue());
                insertList.add(liveStoreSharer);
            });
            channelsSharerMapper.batchInsert(insertList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebind(SharerReBindReqDto reqDto) {
        LiveStoreSharer liveStoreSharer = channelsSharerMapper.selectById(reqDto.getId());
        SharerBindReqDto sharerBindReqDto = new SharerBindReqDto();
        sharerBindReqDto.setStaffId(liveStoreSharer.getStaffId());
        sharerBindReqDto.setId(liveStoreSharer.getId());
        bind(sharerBindReqDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbind(SharerUnBindReqDto reqDto) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException("未获取到用户登录信息");
        }
        LiveStoreSharer liveStoreSharer = channelsSharerMapper.selectById(reqDto.getId());
        if(Objects.isNull(liveStoreSharer)){
            throw new LuckException("未找到记录");
        }
        if(StringUtils.isBlank(liveStoreSharer.getOpenid())){
            throw new LuckException("请检查是否未绑定成功");
        }

        SharerUnbindReq sharerUnbindReq = new SharerUnbindReq();
        sharerUnbindReq.setOpenidList(Lists.newArrayList(liveStoreSharer.getOpenid()));
        log.info("分享员解除绑定入参:{}",JSON.toJSONString(sharerUnbindReq));
        SharerUnbindResp sharerUnbindResp = sharerApi.unbind(wxConfig.getWxEcToken(), sharerUnbindReq);
        log.info("分享员解除绑定出参:{}",JSON.toJSONString(sharerUnbindResp));
        if(sharerUnbindResp.getErrcode() !=0){
            throw new LuckException(sharerUnbindResp.getErrmsg());
        }else{
            if(CollectionUtils.isNotEmpty(sharerUnbindResp.getRefuseOpenid())){
                throw new LuckException("解绑失败:未到解绑时间");
            }
            if(CollectionUtils.isNotEmpty(sharerUnbindResp.getFailOpenid())){
                throw new LuckException("解绑失败:请重试");
            }
            if(CollectionUtils.isNotEmpty(sharerUnbindResp.getSuccessOpenid())){
                liveStoreSharer.setUnbindTime(new Date());
                liveStoreSharer.setUpdatePerson(userInfoInTokenBO.getUserId());
                channelsSharerMapper.updateById(liveStoreSharer);
            }
        }

    }

    @Override
    public PageVO<SharerPageListRespDto> pageList(SharerPageListReqDto reqDto) {
        PageHelper.startPage(reqDto.getPageNum(),reqDto.getPageSize());
        List<LiveStoreSharer> respDtos = channelsSharerMapper.queryList(reqDto);
        PageInfo<LiveStoreSharer> pageInfo = new PageInfo<>(respDtos);

        PageVO<SharerPageListRespDto> pageVO = new PageVO<>();

        if(CollectionUtils.isNotEmpty(pageInfo.getList())){
            pageVO.setList(mapperFacade.mapAsList(pageInfo.getList(), SharerPageListRespDto.class));
            for (SharerPageListRespDto respDto : pageVO.getList()) {
                if (respDto.getUnbindTime() != null) {
                    respDto.setStatus(LiveStoreSharerBindStatus.UNBIND.getValue());
                    respDto.setStatusName(LiveStoreSharerBindStatus.UNBIND.getDesc());
                }else if (respDto.getBindTime() !=null && respDto.getUnbindTime() == null) {
                    respDto.setStatus(LiveStoreSharerBindStatus.BIND_SUCC.getValue());
                    respDto.setStatusName(LiveStoreSharerBindStatus.BIND_SUCC.getDesc());
                }else if (respDto.getQrcodeImgExpireTime().getTime() < new Date().getTime() && respDto.getBindTime() ==null) {
                    respDto.setStatus(LiveStoreSharerBindStatus.BIND_EXPIRE.getValue());
                    respDto.setStatusName(LiveStoreSharerBindStatus.BIND_EXPIRE.getDesc());
                    respDto.setErrorMsg(StringUtils.isNotBlank(respDto.getErrorMsg())?respDto.getErrorMsg():LiveStoreSharerBindStatus.BIND_EXPIRE.getDesc());
                }else if(respDto.getQrcodeImgCreateTime() !=null && respDto.getBindTime() ==null
                        && LiveStoreSharerBindStatus.BIND_WAIT.getValue().equals(respDto.getBindStatus())){
                    respDto.setStatus(LiveStoreSharerBindStatus.BIND_WAIT.getValue());
                    respDto.setStatusName(LiveStoreSharerBindStatus.BIND_WAIT.getDesc());
                } else if (respDto.getQrcodeImgCreateTime()!=null
                        && LiveStoreSharerBindStatus.INIT.getValue().equals(respDto.getBindStatus())) {
                    respDto.setStatus(LiveStoreSharerBindStatus.INIT.getValue());
                    respDto.setStatusName(LiveStoreSharerBindStatus.INIT.getDesc());
                }
                respDto.setSharerTypeName(ShareType.getDesc(respDto.getSharerType()));
            }
        }
        pageVO.setTotal(pageInfo.getTotal());
        pageVO.setPages(pageInfo.getPages());
        return pageVO;
    }

    @Override
    public String getQrImgBase64(Long id) {
        LiveStoreSharer liveStoreSharer = channelsSharerMapper.selectById(id);
        return liveStoreSharer.getQrcodeImgBase64();
    }

    @Override
    public void syncSuccBind() {
        SharerPageListReqDto sharerPageListReqDto = new SharerPageListReqDto();
        sharerPageListReqDto.setStatus(LiveStoreSharerBindStatus.BIND_WAIT.getValue());
        List<LiveStoreSharer> liveStoreSharers = channelsSharerMapper.queryList(sharerPageListReqDto);
        if(CollectionUtils.isNotEmpty(liveStoreSharers)) {
            List<Long> staffIds = liveStoreSharers.stream().map(LiveStoreSharer::getStaffId).collect(Collectors.toList());
            ServerResponseEntity<List<StaffVO>> staffResponse = staffFeignClient.getStaffBypByIds(staffIds);

            if (staffResponse.isSuccess() && CollectionUtils.isNotEmpty(staffResponse.getData())) {
                for (StaffVO staffVO : staffResponse.getData()) {
                    SearchSharerReq searchSharerReq = new SearchSharerReq();
                    searchSharerReq.setUsername(staffVO.getWeChatNo());
                    log.info("获取绑定分享员入参:{}",JSON.toJSONString(searchSharerReq));
                    SearchSharerResp searchSharerResp = sharerApi.searchSharer(wxConfig.getWxEcToken(), searchSharerReq);
                    log.info("获取绑定分享员出参:{}",JSON.toJSONString(searchSharerResp));
                    if (searchSharerResp.getErrcode() == 0 ) {
                        if(searchSharerResp.getBindTime() !=null) {
                            LambdaUpdateWrapper<LiveStoreSharer> lambdaUpdate = Wrappers.lambdaUpdate(LiveStoreSharer.class);
                            lambdaUpdate.eq(LiveStoreSharer::getStaffId, staffVO.getId());
                            lambdaUpdate.set(LiveStoreSharer::getQrcodeImg,null);
                            lambdaUpdate.set(LiveStoreSharer::getQrcodeImgBase64,null);
                            LiveStoreSharer liveStoreSharer = new LiveStoreSharer();
                            liveStoreSharer.setBindTime(DateUtil.date(searchSharerResp.getBindTime() * 1000L));
                            liveStoreSharer.setOpenid(searchSharerResp.getOpenid());
                            liveStoreSharer.setUnionid(searchSharerResp.getUnionid());
                            liveStoreSharer.setSharerType(searchSharerResp.getSharerType());
                            liveStoreSharer.setNickname(searchSharerResp.getNickname());
                            liveStoreSharer.setBindStatus(LiveStoreSharerBindStatus.BIND_SUCC.getValue());
                            channelsSharerMapper.update(liveStoreSharer, lambdaUpdate);
                        }
                    }else{
                        LiveStoreSharer liveStoreSharer = new LiveStoreSharer();
                        liveStoreSharer.setBindStatus(LiveStoreSharerBindStatus.BIND_EXPIRE.getValue());
                        liveStoreSharer.setErrorMsg(searchSharerResp.getErrmsg());
                        liveStoreSharer.setQrcodeImgExpireTime(new Date());
                        channelsSharerMapper.update(liveStoreSharer,
                                Wrappers.lambdaUpdate(LiveStoreSharer.class).eq(LiveStoreSharer::getStaffId, staffVO.getId()));
                    }
                }
            }
        }
    }

    @Override
    public ServerResponseEntity<String> exportQRCode(SharerQrCodeImgListReqDto reqDto) {
        try {

            String time = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
            String fileName=time+"_视频号邀请分享员二维码";
            CalcingDownloadRecordDTO downloadRecordDTO=new CalcingDownloadRecordDTO();
            downloadRecordDTO.setDownloadTime(new Date());
            downloadRecordDTO.setFileName(fileName);
            downloadRecordDTO.setCalCount(0);
            downloadRecordDTO.setOperatorName(AuthUserContext.get().getUsername());
            downloadRecordDTO.setOperatorNo(""+AuthUserContext.get().getUserId());
            ServerResponseEntity<Long> serverResponseEntity=downloadCenterFeignClient.newCalcingTask(downloadRecordDTO);
            Long downLoadHisId=null;
            if(serverResponseEntity.isSuccess()){
                downLoadHisId=Long.parseLong(serverResponseEntity.getData().toString());
            }
            reqDto.setDownLoadHisId(downLoadHisId);
            applicationContext.publishEvent(reqDto);
            return ServerResponseEntity.success("操作成功，请转至下载中心下载");
        }catch (Exception e){
            log.error("导出邀请分享员二维码: {} {}",e,e.getMessage());
            return ServerResponseEntity.showFailMsg("操作失败");
        }
    }

    @Override
    public Long getSharerStaffIdByOpenId(String openid) {
        LiveStoreSharer liveStoreSharer = channelsSharerMapper.selectOne(Wrappers.lambdaQuery(LiveStoreSharer.class)
                .eq(LiveStoreSharer::getOpenid, openid));
        if(liveStoreSharer!=null){
            return liveStoreSharer.getStaffId();
        }
        return null;
    }

    @Override
    public List<ChannelsSharerDto> getByOpenIds(List<String> openids) {
        return channelsSharerMapper.getByOpenIds(openids);
    }

    @Override
    @Transactional
    public void syncBind(LiveStoreSharer liveStoreSharer) {
        boolean succ=true;
        String errorMsg="";
        ServerResponseEntity<StaffVO> staffVOServerResponseEntity = staffFeignClient.getStaffById(liveStoreSharer.getStaffId());
        StaffVO staffVO = staffVOServerResponseEntity.getData();
        if(staffVOServerResponseEntity.isFail() || Objects.isNull(staffVO)){
            errorMsg="未找到员工";
            succ=false;
        }

        SharerBindReq sharerBindReq = new SharerBindReq();
        sharerBindReq.setUsername(staffVOServerResponseEntity.getData().getWeChatNo());
        log.info("绑定分享员入参:{}", JSON.toJSONString(sharerBindReq));
        SharerBindResp sharerBindResp = sharerApi.bind(wxConfig.getWxEcToken(), sharerBindReq);
        log.info("绑定分享员出参:{}",JSON.toJSONString(sharerBindResp));
        if(sharerBindResp.getErrcode() != 0){
            errorMsg=sharerBindResp.getErrmsg();
            succ=false;
        }
        if(succ){
            Date date = new Date();
            LambdaUpdateWrapper<LiveStoreSharer> wrappers = Wrappers.lambdaUpdate(liveStoreSharer)
                    .set(LiveStoreSharer::getQrcodeImgCreateTime,date)
                    .set(LiveStoreSharer::getQrcodeImgExpireTime,DateUtil.offsetDay(date, 3))
                    .set(LiveStoreSharer::getQrcodeImg,Base64Utils.decodeFromString(sharerBindResp.getQrcodeImgBase64()))
                    .set(LiveStoreSharer::getQrcodeImgBase64,sharerBindResp.getQrcodeImgBase64())
                    .set(LiveStoreSharer::getUpdateTime,date)
                    .set(LiveStoreSharer::getOpenid,null)
                    .set(LiveStoreSharer::getBindTime,null)
                    .set(LiveStoreSharer::getUnbindTime,null)
                    .set(LiveStoreSharer::getNickname,null)
                    .set(LiveStoreSharer::getBindStatus,LiveStoreSharerBindStatus.BIND_WAIT.getValue())
                    .eq(LiveStoreSharer::getId,liveStoreSharer.getId());
            channelsSharerMapper.update(liveStoreSharer,wrappers);
        }else{
            LambdaUpdateWrapper<LiveStoreSharer> wrappers = Wrappers.lambdaUpdate(liveStoreSharer)
                    .set(LiveStoreSharer::getErrorMsg,errorMsg)
                    .set(LiveStoreSharer::getQrcodeImgExpireTime,new Date())
                    .set(LiveStoreSharer::getBindStatus,LiveStoreSharerBindStatus.BIND_EXPIRE.getValue())
                    .eq(LiveStoreSharer::getId,liveStoreSharer.getId());
            channelsSharerMapper.update(liveStoreSharer,wrappers);
        }
    }
}
