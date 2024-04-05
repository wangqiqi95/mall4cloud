package com.mall4j.cloud.biz.controller.platform;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.constant.NotifyType;
import com.mall4j.cloud.biz.dto.NotifyTemplateDTO;
import com.mall4j.cloud.biz.model.NotifyTemplate;
import com.mall4j.cloud.biz.service.NotifyTemplateService;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 
 *
 * @author lhd
 * @date 2021-05-14 09:35:32
 */
@RestController("platformNotifyTemplateController")
@RequestMapping("/p/notify_template")
@Api(tags = "platform-消息通知配置")
public class NotifyTemplateController {

    @Autowired
    private NotifyTemplateService notifyTemplateService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<PageVO<NotifyTemplateVO>> page(@Valid PageDTO pageDTO) {

        PageVO<NotifyTemplateVO> templatePage = notifyTemplateService.page(pageDTO);
        if(CollectionUtils.isEmpty(templatePage.getList())){
            return ServerResponseEntity.success();
        }
        for (NotifyTemplateVO template: templatePage.getList()) {
            List<Integer> templateList = getTemplateList(template.getNotifyTypes());
            template.setSms(false);
            template.setSub(false);
            template.setApp(false);
            for (Integer type : templateList) {
                template.setSms(Objects.equals(type, NotifyType.SMS.value()) || template.getSms());
                template.setSub(Objects.equals(type,NotifyType.MP.value()) || template.getSub());
                template.setApp(Objects.equals(type,NotifyType.APP.value()) || template.getApp());
            }
            template.setTemplateTypeList(templateList);
        }
		return ServerResponseEntity.success(templatePage);
	}

	@GetMapping
    @ApiOperation(value = "获取", notes = "根据templateId获取")
    public ServerResponseEntity<NotifyTemplateVO> getByTemplateId(@RequestParam Long templateId) {
        NotifyTemplateVO templateVO = notifyTemplateService.getByTemplateId(templateId);
        templateVO.setTemplateTypeList(getTemplateList(templateVO.getNotifyTypes()));
        return ServerResponseEntity.success(templateVO);
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody NotifyTemplateDTO notifyTemplateDTO) {
        if(CollectionUtils.isEmpty(notifyTemplateDTO.getTemplateTypeList())){
            // 请至少选择一种通知方式
            throw new LuckException("请至少选择一种通知方式");
        }
        int count = notifyTemplateService.countBySendType(notifyTemplateDTO.getSendType(),null);
        if(count > 0){
            // 已经存在当前消息类型的短信，请去进行修改操作
            throw new LuckException("已经存在当前消息类型的短信，请去进行修改操作");
        }
        SendTypeEnum sendType = SendTypeEnum.instance(notifyTemplateDTO.getSendType());
        if(Objects.nonNull(sendType)) {
            notifyTemplateDTO.setMsgType(sendType.getType());
        }
        notifyTemplateDTO.setNotifyTypes(arrayChangeList(notifyTemplateDTO.getTemplateTypeList()));
        NotifyTemplate notifyTemplate = mapperFacade.map(notifyTemplateDTO, NotifyTemplate.class);
        notifyTemplate.setTemplateId(null);
        notifyTemplateService.save(notifyTemplate);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody NotifyTemplateDTO notifyTemplateDTO) {
        if(CollectionUtils.isEmpty(notifyTemplateDTO.getTemplateTypeList())){
            // 请至少选择一种通知方式
            throw new LuckException("请至少选择一种通知方式");
        }
        int count = notifyTemplateService.countBySendType(notifyTemplateDTO.getSendType(),notifyTemplateDTO.getTemplateId());
        if(count > 0){
            // 已经存在当前消息类型的短信，请去进行修改操作
            throw new LuckException("已经存在当前消息类型的短信，请去进行修改操作");
        }
        notifyTemplateDTO.setNotifyTypes(arrayChangeList( notifyTemplateDTO.getTemplateTypeList()));
        SendTypeEnum sendType = SendTypeEnum.instance(notifyTemplateDTO.getSendType());
        if(Objects.nonNull(sendType)) {
            notifyTemplateDTO.setMsgType(sendType.getType());
        }
        NotifyTemplate notifyTemplate = mapperFacade.map(notifyTemplateDTO, NotifyTemplate.class);
        notifyTemplateService.update(notifyTemplate);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除", notes = "根据id删除")
    public ServerResponseEntity<Void> delete(@RequestParam Long templateId) {
        notifyTemplateService.deleteById(templateId);
        return ServerResponseEntity.success();
    }

    /**
     * 通过id状态变更
     * @param templateId id
     * @return 是否修改成功
     */
    @DeleteMapping("/{templateId}" )
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long templateId) {
        NotifyTemplateVO templateVO = notifyTemplateService.getByTemplateId(templateId);
        templateVO.setStatus(templateVO.getStatus() == 1? 0:1);
        NotifyTemplate notifyTemplate = mapperFacade.map(templateVO, NotifyTemplate.class);
        notifyTemplateService.update(notifyTemplate);
        return ServerResponseEntity.success();
    }

    private List<Integer> getTemplateList(String templateTypes) {
        String[] templateTypeList = templateTypes.split(StrUtil.COMMA);
        List<String> asList = Arrays.asList(templateTypeList);
        List<Integer> templates = new ArrayList<>();
        for (String templateStr : asList) {
            templates.add(Integer.valueOf(templateStr));
        }
        return templates;
    }

    private String arrayChangeList(List<Integer> templateTypeList) {
        StringBuilder templateTypes = new StringBuilder();
        for (Integer templateType : templateTypeList) {
            templateTypes.append(templateType);
            templateTypes.append(StrUtil.COMMA);
        }
        templateTypes.deleteCharAt(templateTypes.length() - 1);
        return templateTypes.toString();
    }
}
