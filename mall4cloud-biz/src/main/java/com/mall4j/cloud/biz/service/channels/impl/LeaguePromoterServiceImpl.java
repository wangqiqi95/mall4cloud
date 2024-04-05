package com.mall4j.cloud.biz.service.channels.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.api.biz.constant.channels.PromoterCooperateEnum;
import com.mall4j.cloud.api.biz.dto.channels.request.league.promoter.LeaguePromoterListGetReq;
import com.mall4j.cloud.api.biz.dto.channels.request.league.promoter.LeaguePromoterReq;
import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.LeaguePromoterInfoListResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.LeaguePromoterInfoResp;
import com.mall4j.cloud.api.biz.dto.channels.response.league.promoter.PromoterResp;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.biz.wx.wx.channels.LeaguePromoterApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.channels.league.*;
import com.mall4j.cloud.biz.mapper.channels.LeaguePromoterMapper;
import com.mall4j.cloud.biz.model.channels.LeaguePromoter;
import com.mall4j.cloud.biz.service.channels.LeaguePromoterService;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 视频号优选联盟
 * @Author axin
 * @Date 2023-02-14 17:52
 **/
@Slf4j
@Service
public class LeaguePromoterServiceImpl implements LeaguePromoterService {

    @Resource
    private LeaguePromoterMapper leaguePromoterMapper;
    @Resource
    private WxConfig wxConfig;
    @Resource
    private LeaguePromoterApi leaguePromoterApi;
    @Resource
    private MapperFacade mapperFacade;
    @Resource
    private StoreFeignClient storeFeignClient;


    @Override
    public PageVO<PromoterListRespDto> promoterList(PromoterListReqDto reqDto) {
        PageVO<PromoterListRespDto> respPage = new PageVO<>();
        List<String> finders = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(reqDto.getFinderIds())){
            finders.addAll(reqDto.getFinderIds());
        }
        if (reqDto.getStatus() != null){
            int pageNo=1;
            int totalPage=1;
            do{
                LeaguePromoterListGetReq leaguePromoterListGetReq = new LeaguePromoterListGetReq();
                leaguePromoterListGetReq.setStatus(reqDto.getStatus());
                leaguePromoterListGetReq.setPageIndex(reqDto.getPageNum());
                leaguePromoterListGetReq.setPageSize(200);
                LeaguePromoterInfoListResp listResp = leaguePromoterApi.getList(wxConfig.getWxEcTokenTest(), leaguePromoterListGetReq);
                if(listResp.getErrcode()!=0){
                    throw new LuckException(listResp.getErrmsg());
                }
                if(CollectionUtils.isNotEmpty(listResp.getFinderIds())){
                    finders.addAll(listResp.getFinderIds());
                }

                totalPage = PageUtil.getPages(listResp.getTotalNum(), 200);
                pageNo++;
            }while (pageNo<=totalPage);

        }
        LambdaQueryWrapper<LeaguePromoter> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(reqDto.getFinderName()), LeaguePromoter::getFinderName, reqDto.getFinderName())
                .in(CollectionUtils.isNotEmpty(reqDto.getStoreIds()), LeaguePromoter::getStoreId, reqDto.getStoreIds())
                .in(CollectionUtils.isNotEmpty(finders), LeaguePromoter::getFinderId, finders)
                .isNull(CollectionUtils.isEmpty(finders) && Objects.nonNull(reqDto.getStatus()),LeaguePromoter::getId);
        Page<LeaguePromoter> promoterPage = leaguePromoterMapper.selectPage(new Page<>(reqDto.getPageNum(), reqDto.getPageSize()), lambdaQueryWrapper);

        respPage.setList(mapperFacade.mapAsList(promoterPage.getRecords(), PromoterListRespDto.class));

        List<Long> storeIds = respPage.getList().stream().map(PromoterListRespDto::getStoreId).collect(Collectors.toList());
        Map<Long, StoreVO> storeVOMap = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(storeIds)){
            ServerResponseEntity<List<StoreVO>> storeResponseEntity = storeFeignClient.listByStoreIdList(storeIds);
            if(storeResponseEntity.isSuccess() && CollectionUtils.isNotEmpty(storeResponseEntity.getData())){
                storeVOMap = storeResponseEntity.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, k -> k));
            }
        }

        for (PromoterListRespDto promoterListRespDto : respPage.getList()) {
            LeaguePromoterReq leaguePromoterReq = new LeaguePromoterReq();
            leaguePromoterReq.setFinderId(promoterListRespDto.getFinderId());
//            log.info("调用获取达人入参:{},{}",JSON.toJSONString(leaguePromoterReq),wxConfig.getWxEcTokenTest());
//            LeaguePromoterInfoResp resp = leaguePromoterApi.get(wxConfig.getWxEcTokenTest(), leaguePromoterReq);
//            log.info("调用获取达人出参:{}",JSON.toJSONString(resp));

//            if(resp.getErrcode()!=0){
//                throw new LuckException(resp.getErrmsg());
//            }
            StoreVO storeVO = storeVOMap.get(promoterListRespDto.getStoreId());
            promoterListRespDto.setStoreCode(Objects.isNull(storeVO) ? "" : storeVO.getStoreCode());
            promoterListRespDto.setStoreName(Objects.isNull(storeVO) ? "" : storeVO.getName());
//            promoterListRespDto.setStatus(resp.getPromoter().getStatus());
//            promoterListRespDto.setStatusName(PromoterCooperateEnum.getDesc(resp.getPromoter().getStatus()));
        }
        respPage.setPages((int) promoterPage.getPages());
        respPage.setTotal(promoterPage.getTotal());
        return respPage;
    }

    @Override
    public PromoterDetailRespDto promoterDetail(Long id) {
        LeaguePromoter promoter = leaguePromoterMapper.selectById(id);
        if(Objects.isNull(promoter)){
            throw new LuckException("达人不存在");
        }
        PromoterDetailRespDto respDto = new PromoterDetailRespDto();
        respDto.setId(promoter.getId());
        respDto.setFinderId(promoter.getFinderId());
        respDto.setFinderName(promoter.getFinderName());
        respDto.setStoreId(promoter.getStoreId());

        StoreVO store = storeFeignClient.findByStoreId(promoter.getStoreId());
        if(Objects.nonNull(store)){
            respDto.setStoreName(store.getName());
        }

        LeaguePromoterReq leaguePromoterReq = new LeaguePromoterReq();
        leaguePromoterReq.setFinderId(promoter.getFinderId());
        log.info("调用视频号达人详情接口:{}",JSON.toJSONString(leaguePromoterReq));
        LeaguePromoterInfoResp leaguePromoterInfoResp = leaguePromoterApi.get(wxConfig.getWxEcTokenTest(), leaguePromoterReq);
        log.info("调用视频号达人返回详情:{}", JSON.toJSONString(leaguePromoterInfoResp));
        if(leaguePromoterInfoResp.getErrcode()!=0){
            throw new LuckException(leaguePromoterInfoResp.getErrmsg());
        }
        respDto.setStatus(leaguePromoterInfoResp.getPromoter().getStatus());
        respDto.setStatusName(PromoterCooperateEnum.getDesc(respDto.getStatus()));
        respDto.setSaleProductNumber(leaguePromoterInfoResp.getPromoter().getSaleProductNumber());
        respDto.setSaleGmv(leaguePromoterInfoResp.getPromoter().getSaleGmv());

        return respDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void promoterAdd(PromoterAddReqDto reqDto) {
        LambdaQueryWrapper<LeaguePromoter> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(LeaguePromoter::getFinderId,reqDto.getFinderId());
        LeaguePromoter livestoreLeaguePromoter = leaguePromoterMapper.selectOne(lambdaQueryWrapper);
        if(Objects.nonNull(livestoreLeaguePromoter)){
            throw new LuckException("该达人已存在,请勿重复添加");
        }

        LeaguePromoterReq leaguePromoterReq = new LeaguePromoterReq();
        leaguePromoterReq.setFinderId(reqDto.getFinderId());
        log.info("调用视频号添加达人入参:{}",leaguePromoterReq);
        BaseResponse response = leaguePromoterApi.add(wxConfig.getWxEcTokenTest(), leaguePromoterReq);
        log.info("调用视频号添加达人出参:{}",response);
        if(response.getErrcode()!=0){
            throw new LuckException(response.getErrmsg());
        }
        LeaguePromoter leaguePromoter = mapperFacade.map(reqDto, LeaguePromoter.class);
        Date date = new Date();
        leaguePromoter.setCreateTime(date);
        leaguePromoter.setUpdateTime(date);
        leaguePromoterMapper.insert(leaguePromoter);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void promoterUpd(PromoterUpdReqDto reqDto) {
        LeaguePromoter oldPromoter = leaguePromoterMapper.selectById(reqDto.getId());
        if(Objects.isNull(oldPromoter)){
            throw new LuckException("未找到要修改的达人");
        }
        if(!oldPromoter.getFinderId().equals(reqDto.getFinderId())){
            LeaguePromoterReq leaguePromoterReq = new LeaguePromoterReq();
            log.info("调用视频号查询达人入参:{}",JSON.toJSONString(leaguePromoterReq));
            LeaguePromoterInfoResp resp = leaguePromoterApi.get(wxConfig.getWxEcTokenTest(), leaguePromoterReq);
            log.info("调用视频号查询达人出参:{}",JSON.toJSONString(resp));
            if(resp.getErrcode()!=0){
                throw new LuckException(resp.getErrmsg());
            }
            if(Objects.equals(resp.getPromoter().getStatus(), PromoterCooperateEnum.INVITATION_ACCEPTED.getValue())
               ||Objects.equals(resp.getPromoter().getStatus(), PromoterCooperateEnum.INVITED.getValue())){
                throw new LuckException("达人邀请中或已接受邀请,无法修改视频号ID");
            }
        }
        LeaguePromoter leaguePromoter = new LeaguePromoter();
        leaguePromoter.setId(oldPromoter.getId());
        leaguePromoter.setFinderId(reqDto.getFinderId());
        leaguePromoter.setFinderName(reqDto.getFinderName());
        leaguePromoter.setStoreId(reqDto.getStoreId());
        leaguePromoterMapper.updateById(leaguePromoter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void promoterDel(PromoterDelReqDto reqDto) {
        LeaguePromoter livestoreLeaguePromoter = leaguePromoterMapper.selectById(reqDto.getId());
        if(livestoreLeaguePromoter == null){
            throw new LuckException("未找到记录");
        }

        LeaguePromoterReq leaguePromoterReq= new LeaguePromoterReq();
        leaguePromoterReq.setFinderId(livestoreLeaguePromoter.getFinderId());
        log.info("调用视频号删除达人入参:{}",JSON.toJSONString(leaguePromoterReq));
        BaseResponse response = leaguePromoterApi.delete(wxConfig.getWxEcTokenTest(), leaguePromoterReq);
        log.info("调用视频号删除达人出参:{}",JSON.toJSONString(response));

        if(response.getErrcode() !=0){
            throw new LuckException(response.getErrmsg());
        }

        leaguePromoterMapper.deleteById(reqDto.getId());

    }

    @Override
    public PromoterResp getPromoterByFinderId(String finderId) {
        List<LeaguePromoter> leaguePromoters = leaguePromoterMapper.selectList(Wrappers.lambdaQuery(LeaguePromoter.class).eq(LeaguePromoter::getFinderId, finderId));
        if(CollectionUtils.isNotEmpty(leaguePromoters)){
            LeaguePromoter leaguePromoter = leaguePromoters.get(0);
            return mapperFacade.map(leaguePromoter,PromoterResp.class);
        }
        return null;
    }
}
