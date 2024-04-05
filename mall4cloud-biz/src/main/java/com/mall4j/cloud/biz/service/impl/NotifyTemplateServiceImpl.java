package com.mall4j.cloud.biz.service.impl;

import com.mall4j.cloud.biz.mapper.NotifyLogMapper;
import com.mall4j.cloud.biz.mapper.NotifyTemplateMapper;
import com.mall4j.cloud.biz.model.NotifyTemplate;
import com.mall4j.cloud.biz.service.NotifyTemplateService;
import com.mall4j.cloud.biz.service.SendMessageService;
import com.mall4j.cloud.biz.vo.NotifyParamVO;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.biz.vo.SmsCodeTemplateVO;
import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
@Service
public class NotifyTemplateServiceImpl implements NotifyTemplateService {

    @Autowired
    private NotifyTemplateMapper notifyTemplateMapper;
    @Autowired
    private NotifyLogMapper notifyLogMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SendMessageService sendMessageService;

    @Override
    public PageVO<NotifyTemplateVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> notifyTemplateMapper.list());
    }

    @Override
    public NotifyTemplateVO getByTemplateId(Long templateId) {
        return notifyTemplateMapper.getByTemplateId(templateId);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SMS_CODE_KEY, key = "#notifyTemplate.sendType")
    public void save(NotifyTemplate notifyTemplate) {
        notifyTemplateMapper.save(notifyTemplate);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SMS_CODE_KEY, key = "#notifyTemplate.sendType")
    public void update(NotifyTemplate notifyTemplate) {
        notifyTemplateMapper.update(notifyTemplate);
    }

    @Override
    public void deleteById(Long templateId) {
        notifyTemplateMapper.deleteById(templateId);
    }

    @Override
    public NotifyTemplateVO getBySendType(Integer sendType) {
        return notifyTemplateMapper.getBySendType(sendType);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SMS_CODE_KEY, key = "#sendType")
    public SmsCodeTemplateVO getSmsCodeTemplateBySendType(Integer sendType) {
        return notifyTemplateMapper.getSmsCodeTemplateBySendType(sendType);
    }

    @Override
    public Integer countBySendType(Integer sendType, Long templateId) {
        return notifyTemplateMapper.countBySendType(sendType,templateId);
    }

    /**
     * 检查通知是否已经发送
     * @param orderId 订单id
     * @param level 关联id
     * @param userId 接受通知的用户id（店铺id）
     * @param sendType 发送类型
     * @return 结果
     */
    private boolean checkNotifyIsSend(Long orderId, Integer level,Long userId,Integer sendType) {
        int num = notifyLogMapper.countNotifyByConditions(orderId,level,userId,sendType);
        return num > 0;
    }

}
