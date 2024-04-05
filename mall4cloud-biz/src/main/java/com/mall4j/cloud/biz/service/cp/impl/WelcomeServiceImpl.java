package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.CpWelcomeTimeStateDTO;
import com.mall4j.cloud.biz.dto.cp.WelcomeDTO;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.mapper.cp.WelcomeAttachmentMapper;
import com.mall4j.cloud.biz.mapper.cp.WelcomeMapper;
import com.mall4j.cloud.biz.model.cp.CpWelcomeTimeState;
import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import com.mall4j.cloud.biz.model.cp.CpWelcome;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.service.cp.CpWelcomeTimeStateService;
import com.mall4j.cloud.biz.service.cp.StoreWelcomeConfigService;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;
import com.mall4j.cloud.biz.service.cp.WelcomeService;
import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import com.mall4j.cloud.biz.vo.cp.WelcomeDetailPlusVO;
import com.mall4j.cloud.biz.vo.cp.WelcomeDetailVO;
import com.mall4j.cloud.biz.vo.cp.WelcomeVO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 欢迎语配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WelcomeServiceImpl extends ServiceImpl<WelcomeMapper, CpWelcome> implements WelcomeService {
    private final  WelcomeMapper welcomeMapper;
    private final StoreWelcomeConfigService shopWelcomeConfigService;
    private final WelcomeAttachmentService welcomeAttachmentService;
    private final WelcomeAttachmentMapper welcomeAttachmentMapper;
    private final WelcomeAttachmentService attachmentService;
    private final MapperFacade mapperFacade;
    private final CpWelcomeTimeStateService cpWelcomeTimeStateService;

    @Override
    public PageVO<WelcomeVO> page(PageDTO pageDTO, WelcomeDTO request) {
        PageVO<WelcomeVO> pageVO=PageUtil.doPage(pageDTO, () -> welcomeMapper.list(request));

        pageVO.getList().stream().forEach(welcomeVO -> {
            if(StrUtil.isNotBlank(welcomeVO.getSlogan())){
                welcomeVO.setSlogan(SlognUtils.parseSlogn(welcomeVO.getSlogan()));
            }
        });

        return pageVO;
    }

    @Override
    public CpWelcome getById(Long id) {
        CpWelcome welcome=welcomeMapper.getById(id);
        if(welcome==null){
            Assert.faild("当前记录不存在，请检查后再试。");
        }
        if(StrUtil.isNotBlank(welcome.getSlogan())){
            welcome.setSlogan(SlognUtils.parseSlogn(welcome.getSlogan()));
        }
        return welcome;
    }

    @Override
    public WelcomeDetailPlusVO getWelcomeDetailByWeight(Long storeId) {
        CpWelcome welcome = this.getWelcomeByWeight(storeId);
        if(Objects.isNull(welcome)){
            return null;
        }

        List<CpWelcomeAttachment> attachments = attachmentService.listByWelId(welcome.getId(), OriginType.WEL_CONFIG.getCode());
        return new WelcomeDetailPlusVO(welcome,attachments,null);
    }

    @Override
    public WelcomeDetailVO getWelcomeDetailSimpleByWeight(Long storeId) {
        CpWelcome welcome = this.getWelcomeByWeight(storeId);
        if(Objects.isNull(welcome)){
            return null;
        }
        CpWelcomeAttachment attachments = attachmentService.getAttachmentByWelId(welcome.getId(), OriginType.WEL_CONFIG.getCode());
        return new WelcomeDetailVO(welcome,attachments,null);
    }

    /**
     * 保存渠道活码附件信息
     * @param sourceId
     * @param welcomeDTOS
     */
    @Override
    public void saveStaffCodeWelcome(Long sourceId, List<ChannelCodeWelcomeDTO> welcomeDTOS) {
        //先删除
        welcomeMapper.deleteBySourceId(sourceId, Arrays.asList(1,2));
        welcomeAttachmentMapper.deleteBySourceId(sourceId,OriginType.STAFF_CODE.getCode());

        for (ChannelCodeWelcomeDTO welcomeDTO : welcomeDTOS) {
            if(StrUtil.isNotBlank(welcomeDTO.getSlogan())){
                welcomeDTO.setSlogan(SlognUtils.formatSlogn(welcomeDTO.getSlogan()));
            }
            CpWelcome welcome=mapperFacade.map(welcomeDTO, CpWelcome.class);
            welcome.setSourceId(sourceId);
            welcome.setFlag(0);
            welcome.setCreateName(AuthUserContext.get().getUsername());
            if(welcome.getSourceFrom()==1){//源：0欢迎语/1渠道活码/2渠道活码时间段
                welcome.setTitle("渠道活码附件");
            }else{
                welcome.setTitle("渠道活码时间段附件");
            }
            this.save(welcome);

            List<CpWelcomeAttachment> attachMents=new ArrayList<>();

            List<AttachmentExtDTO> attachMentDTOS = AttachMentVO.getAttachMents(welcomeDTO.getAttachMents());
            attachMentDTOS.stream().forEach(attachMent->{
                CpWelcomeAttachment attachment= new CpWelcomeAttachment(attachMent, OriginType.STAFF_CODE, welcome.getId(),sourceId);
                attachment.setMaterialId(attachMent.getMaterialId());
                attachMents.add(attachment);
            });

            welcomeAttachmentService.saveBatch(attachMents);
        }

    }

    @Override
    public void enable(Long id) {
        CpWelcome welcome=welcomeMapper.getById(id);
        if(welcome==null){
            Assert.faild("欢迎语不存在，请检查数据后再操作。");
        }

        List<ShopWelcomeConfig> shops = shopWelcomeConfigService.listByWelId(id);
        List<Long> shopIds = shops.stream().map(ShopWelcomeConfig::getShopId).collect(Collectors.toList());

        CpWelcome welcomes =  welcomeMapper.checkonly(welcome.getScene(),shopIds);
        if( welcomes!=null ){
            String errorMsg =  StrUtil.format("已存在相同欢迎语场景，ID:【{}】",welcomes.getId());
            Assert.faild(errorMsg);
        }

        this.update(new LambdaUpdateWrapper<CpWelcome>().eq(CpWelcome::getId,id).set(CpWelcome::getStatus,1));
    }

    @Override
    public void disable(Long id) {
        CpWelcome welcome=welcomeMapper.getById(id);
        if(welcome==null){
            Assert.faild("欢迎语不存在，请检查数据后再操作。");
        }

        this.update(new LambdaUpdateWrapper<CpWelcome>().eq(CpWelcome::getId,id).set(CpWelcome::getStatus,0));
    }

    @Override
    public boolean saveWelcome(CpWelcome welcome) {
       return this.save(welcome);
    }

    @Override
    public void updateWelcome(CpWelcome welcome) {
        this.updateById(welcome);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
//        welcomeAttachmentService.deleteByWelId(id, OriginType.WEL_CONFIG.getCode());
//        shopWelcomeConfigService.deleteByWelId(id);
        welcomeMapper.logicDelete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createWelcome(CpWelcome welcome, List<AttachmentExtDTO> attachMent, List<Long> shops,WelcomeDTO welcomeDTO) {
        if(StrUtil.isNotBlank(welcome.getSlogan())){
            welcome.setSlogan(SlognUtils.formatSlogn(welcome.getSlogan()));
        }
        welcome.setSourceFrom(0);
        if(welcome.getId()==null) {
            this.save(welcome);
        }else {
            welcomeAttachmentService.deleteByWelId(welcome.getId(), OriginType.WEL_CONFIG.getCode());
            shopWelcomeConfigService.deleteByWelId(welcome.getId());
            cpWelcomeTimeStateService.deleteByWelId(welcome.getId());
            this.updateById(welcome);
        }

        //分时段欢迎语
        if(welcomeDTO.getWelcomeTimeState()!=0 && CollUtil.isNotEmpty(welcomeDTO.getWelcomeTimeStateDTOS())){

            //校验接待欢迎语分时段冲突
            cpWelcomeTimeStateService.checkAttachMentTime(welcomeDTO.getWelcomeTimeStateDTOS());

            for (CpWelcomeTimeStateDTO welcomeTimeStateDTO : welcomeDTO.getWelcomeTimeStateDTOS()) {
                CpWelcomeTimeState timeState = mapperFacade.map(welcomeTimeStateDTO, CpWelcomeTimeState.class);
                if(StrUtil.isNotEmpty(timeState.getTimeEnd()) && timeState.getTimeEnd().equals("24:00")){//如果前端传的时间为24:00,报存为23:59
                    timeState.setTimeEnd("23:59");
                }
                timeState.setSlogan(SlognUtils.formatSlogn(timeState.getSlogan()));
                timeState.setWelId(welcome.getId());
                cpWelcomeTimeStateService.save(timeState);
                //保存分时段欢迎语附件
                List<AttachmentExtDTO> timeAttachMent = AttachMentVO.getAttachMents(welcomeTimeStateDTO.getAttachMentBaseDTOS());
                if(CollUtil.isEmpty(timeAttachMent)){
                    continue;
                }
                for (AttachmentExtDTO attachmentExtDTO : timeAttachMent) {
                    CpWelcomeAttachment welcomeAttachment = new CpWelcomeAttachment();
                    welcomeAttachment.setData(Json.toJsonString(attachmentExtDTO));
                    welcomeAttachment.setType(attachmentExtDTO.getAttachment().getMsgType());
                    welcomeAttachment.setWelId(welcome.getId());
                    welcomeAttachment.setSourceId(welcome.getId());
                    welcomeAttachment.setOrigin(OriginType.WEL_CONFIG.getCode());
                    welcomeAttachment.setUpdateTime(welcome.getCreateTime());
                    welcomeAttachment.setTimeStateId(timeState.getId());
                    welcomeAttachment.setMaterialId(attachmentExtDTO.getMaterialId());
                    welcomeAttachmentService.save(welcomeAttachment);
                }
            }
        }

        if(!CollectionUtils.isEmpty(attachMent)) {
            for (AttachmentExtDTO attachmentExtDTO : attachMent) {
                CpWelcomeAttachment welcomeAttachment = new CpWelcomeAttachment();
                welcomeAttachment.setData(Json.toJsonString(attachmentExtDTO));
                welcomeAttachment.setType(attachmentExtDTO.getAttachment().getMsgType());
                welcomeAttachment.setWelId(welcome.getId());
                welcomeAttachment.setSourceId(welcome.getId());
                welcomeAttachment.setOrigin(OriginType.WEL_CONFIG.getCode());
                welcomeAttachment.setUpdateTime(welcome.getCreateTime());
                welcomeAttachment.setMaterialId(attachmentExtDTO.getMaterialId());
                welcomeAttachmentService.save(welcomeAttachment);
            }
        }

        if(!CollectionUtils.isEmpty(shops)) {
            for (Long shopId : shops) {
                ShopWelcomeConfig welcomeConfig = new ShopWelcomeConfig();
                welcomeConfig.setShopId(shopId);
                welcomeConfig.setType(0);
                welcomeConfig.setCreateTime(new Date());
                welcomeConfig.setWelId(welcome.getId());
                welcomeConfig.setUpdateTime(welcomeConfig.getCreateTime());
                shopWelcomeConfigService.save(welcomeConfig);
            }
        }

        if(!CollectionUtils.isEmpty(welcomeDTO.getStaffIds())) {
            for (Long staffId : welcomeDTO.getStaffIds()) {
                ShopWelcomeConfig welcomeConfig = new ShopWelcomeConfig();
                welcomeConfig.setShopId(staffId);
                welcomeConfig.setType(1);
                welcomeConfig.setFlag(0);
                welcomeConfig.setCreateTime(new Date());
                welcomeConfig.setWelId(welcome.getId());
                welcomeConfig.setUpdateTime(welcomeConfig.getCreateTime());
                shopWelcomeConfigService.save(welcomeConfig);
            }
        }


    }

    @Override
    public CpWelcome getWelcomeByWeight(Long storeId) {
        return welcomeMapper.getWelcomeByWeight(storeId);
    }

    @Override
    public CpWelcome getWelcomeByOrgs(List<Long> orgs, Integer sence, Integer source_from) {
        Long id = welcomeMapper.getWelcomeIdByOrgs(orgs,sence,source_from);
        if(id==null){
            return null;
        }
        return welcomeMapper.getById(id);
    }
}
