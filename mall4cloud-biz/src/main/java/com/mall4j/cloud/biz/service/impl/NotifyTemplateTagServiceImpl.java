package com.mall4j.cloud.biz.service.impl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.user.feign.UserTagClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserTagApiVO;
import com.mall4j.cloud.biz.constant.NotifyType;
import com.mall4j.cloud.biz.dto.NotifyTemplateDTO;
import com.mall4j.cloud.biz.mapper.NotifyTemplateTagMapper;
import com.mall4j.cloud.biz.model.NotifyLog;
import com.mall4j.cloud.biz.model.NotifyTemplate;
import com.mall4j.cloud.biz.model.NotifyTemplateTag;
import com.mall4j.cloud.biz.service.NotifyLogService;
import com.mall4j.cloud.biz.service.NotifyTemplateService;
import com.mall4j.cloud.biz.service.NotifyTemplateTagService;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 *
 * @author cl
 * @date 2021-05-20 11:09:53
 */
@Service
public class NotifyTemplateTagServiceImpl implements NotifyTemplateTagService {

    @Autowired
    private NotifyTemplateTagMapper notifyTemplateTagMapper;

    @Autowired
    private NotifyTemplateService notifyTemplateService;

    @Autowired
    private NotifyLogService notifyLogService;

    @Autowired
    private UserTagClient userTagClient;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageVO<NotifyTemplateTag> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> notifyTemplateTagMapper.list());
    }

    @Override
    public NotifyTemplateTag getByNotifyTagId(Long notifyTagId) {
        return notifyTemplateTagMapper.getByNotifyTagId(notifyTagId);
    }

    @Override
    public void save(NotifyTemplateTag notifyTemplateTag) {
        notifyTemplateTagMapper.save(notifyTemplateTag);
    }

    @Override
    public void update(NotifyTemplateTag notifyTemplateTag) {
        notifyTemplateTagMapper.update(notifyTemplateTag);
    }

    @Override
    public void deleteById(Long notifyTagId) {
        notifyTemplateTagMapper.deleteById(notifyTagId);
    }

    @Override
    public PageVO<NotifyTemplateVO> pageTagNotify(PageDTO pageDTO) {
        PageVO<NotifyTemplateVO> pageVO = new PageVO<>();
        int count = notifyTemplateTagMapper.countTagNotify();
        pageVO.setTotal(Long.valueOf(count));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        if (count < 1) {
            return pageVO;
        }
        List<NotifyTemplateVO> list = notifyTemplateTagMapper.pageTagNotify(new PageAdapter(pageDTO));
        List<List<Long>> collect = list.stream().map(NotifyTemplateVO::getTagIds).distinct().collect(Collectors.toList());
        Set<Long> tagIds = new HashSet<>();
        for (List<Long> longs : collect) {
            tagIds.addAll(longs);
        }

        // 获取标签名称
        ServerResponseEntity<List<UserTagApiVO>> responseEntity = userTagClient.getUserTagList(new ArrayList<>(tagIds));
        if (!Objects.equals(responseEntity.getCode(), ResponseEnum.OK.value())) {
            throw new LuckException(responseEntity.getMsg());
        }
        List<UserTagApiVO> tagApiVOList = responseEntity.getData();
        Map<Long, UserTagApiVO> tagApiMap = tagApiVOList.stream().collect(Collectors.toMap(UserTagApiVO::getTagId, (k) -> k));
        for (NotifyTemplateVO notifyTemplateVO : list) {
            notifyTemplateVO.setApp(true);
            List<Long> tagIdList = notifyTemplateVO.getTagIds();
            // 拼接字符串
            StringBuilder tagNames = new StringBuilder(100);
            for (Long tagId : tagIdList) {
                UserTagApiVO userTagApiVO = tagApiMap.get(tagId);
                if (Objects.nonNull(userTagApiVO)){
                    tagNames.append(userTagApiVO.getTagName()).append(StrUtil.COMMA);
                }
            }
            if(tagNames.length() > 1) {
                tagNames.subSequence(0, Math.min(tagNames.length() - 1, 100));
                if (tagNames.lastIndexOf(",") == tagNames.length() - 1) {
                    tagNames.deleteCharAt(tagNames.length() - 1);
                }
                notifyTemplateVO.setTemplateCode(tagNames.toString());
            }
        }
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagNotify(NotifyTemplate notifyTemplate,List<Long> tagIds) {
        notifyTemplateService.save(notifyTemplate);
        List<NotifyTemplateTag> notifyTemplateTags = new ArrayList<>();
        for (Long tagId : tagIds) {
            NotifyTemplateTag notifyTemplateTag = new NotifyTemplateTag();
            notifyTemplateTag.setUserTagId(tagId);
            notifyTemplateTag.setTemplateId(notifyTemplate.getTemplateId());
            notifyTemplateTags.add(notifyTemplateTag);
        }
        notifyTemplateTagMapper.saveBatch(notifyTemplateTags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateTagByTempLateId(Long templateId) {
        notifyTemplateService.deleteById(templateId);
        notifyTemplateTagMapper.deleteTemplateTagByTempLateId(templateId);
    }

    @Override
    public void sendMsg(Long templateId) {
        NotifyTemplateVO notifyTemplateVO = notifyTemplateService.getByTemplateId(templateId);
        if (Objects.isNull(notifyTemplateVO)) {
            throw new LuckException("消息模板不存在！");
        }
        // 获取标签下的所有标签id
        List<Long> userTagIds = notifyTemplateTagMapper.getTagIdsByTemplateId(templateId);
        if (CollUtil.isEmpty(userTagIds)) {
            return;
        }
        ServerResponseEntity<List<UserApiVO>> userResponse = userTagClient.getUserByTagIds(userTagIds);
        if (!Objects.equals(ResponseEnum.OK.value(),userResponse.getCode())) {
            throw new LuckException(userResponse.getMsg());
        }
        List<UserApiVO> userApiVOList = userResponse.getData();
        if (CollUtil.isEmpty(userApiVOList)) {
            throw new LuckException("没有符合条件的用户去发送这条消息");
        }
        List<NotifyLog> logs = new ArrayList<>();
        for (UserApiVO userApiVO : userApiVOList) {
            NotifyLog notifyLog = new NotifyLog();
            notifyLog.setNickName(userApiVO.getNickName());
            notifyLog.setUserMobile(userApiVO.getUserMobile());
            // 如果大于则为店铺自行接收的消息
            notifyLog.setRemindId(userApiVO.getUserId().toString());
            notifyLog.setShopId(Constant.PLATFORM_SHOP_ID);
            // 站内消息
            notifyLog.setRemindType(NotifyType.APP.value());
            notifyLog.setTemplateId(templateId);
            notifyLog.setSendType(SendTypeEnum.CUSTOMIZE.getValue());
            notifyLog.setMessage(notifyTemplateVO.getMessage());
            notifyLog.setStatus(0);
            logs.add(notifyLog);
        }
        notifyLogService.saveBatch(logs);
    }

    @Override
    public NotifyTemplateVO getByTemplateId(Long templateId) {
        return notifyTemplateTagMapper.getByTemplateId(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateAndTag(NotifyTemplateDTO notifyTemplateDTO) {
        NotifyTemplate notifyTemplate = mapperFacade.map(notifyTemplateDTO, NotifyTemplate.class);
        notifyTemplateService.update(notifyTemplate);
        List<Long> tagIds = notifyTemplateDTO.getTagIds();
        Long templateId = notifyTemplate.getTemplateId();
        notifyTemplateTagMapper.deleteTemplateTagByTempLateId(templateId);
        if (CollUtil.isEmpty(tagIds)) {
            return;
        }
        List<NotifyTemplateTag> notifyTemplateTags = new ArrayList<>();
        for (Long tagId : tagIds) {
            NotifyTemplateTag notifyTemplateTag = new NotifyTemplateTag();
            notifyTemplateTag.setTemplateId(templateId);
            notifyTemplateTag.setUserTagId(tagId);
            notifyTemplateTags.add(notifyTemplateTag);
        }
        notifyTemplateTagMapper.saveBatch(notifyTemplateTags);
    }

    @Override
    public void deleteByTagId(Long tagId) {
        notifyTemplateTagMapper.deleteTemplateTagByTagId(tagId);
    }
}
