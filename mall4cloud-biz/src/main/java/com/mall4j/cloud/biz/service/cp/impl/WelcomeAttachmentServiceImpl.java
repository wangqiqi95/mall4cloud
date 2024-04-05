package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.WelcomeAttachmentMapper;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 欢迎语附件列表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@RequiredArgsConstructor
@Service
public class WelcomeAttachmentServiceImpl extends ServiceImpl<WelcomeAttachmentMapper, CpWelcomeAttachment> implements WelcomeAttachmentService {

    private final  WelcomeAttachmentMapper welcomeAttachmentMapper;

    @Override
    public boolean saveWelcomeAttachment(CpWelcomeAttachment welcomeAttachment) {
        return this.save(welcomeAttachment);
    }

    @Override
    public void deleteByWelId(Long welId,int orgion) {
        welcomeAttachmentMapper.deleteByWelId(welId,orgion);
    }

    @Override
    public CpWelcomeAttachment selectOneByWelId(Long welId, int orgion) {
         List<CpWelcomeAttachment> list =  welcomeAttachmentMapper.listByWelId(welId,orgion) ;
         if(!CollectionUtils.isEmpty(list)){
             return list.get(0);
         }
         return null;
    }

    @Override
    public List<CpWelcomeAttachment> listByWelId(Long welId, int origin) {
        List<CpWelcomeAttachment> list =  welcomeAttachmentMapper.listByWelId(welId,origin) ;
        return list;
    }

    @Override
    public CpWelcomeAttachment getAttachmentByWelId(Long welId, int origin) {
        return welcomeAttachmentMapper.selectAttachmentByWelId(welId,origin);
    }

    @Override
    public List<CpWelcomeAttachment> listAfterThreeDayPicMediaIds() {
        return welcomeAttachmentMapper.listAfterThreeDayPicMediaIds();
    }

    @Override
    public void updateWelcomeAttachment(CpWelcomeAttachment welcomeAttachment) {
        this.updateById(welcomeAttachment);
    }
}
