package com.mall4j.cloud.biz.controller.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.constant.NotifyType;
import com.mall4j.cloud.biz.dto.NotifyTemplateDTO;
import com.mall4j.cloud.biz.model.NotifyTemplate;
import com.mall4j.cloud.biz.model.NotifyTemplateTag;
import com.mall4j.cloud.biz.service.NotifyTemplateService;
import com.mall4j.cloud.biz.service.NotifyTemplateTagService;
import com.mall4j.cloud.biz.vo.NotifyTemplateTagVO;
import com.mall4j.cloud.biz.dto.NotifyTemplateTagDTO;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.biz.vo.NotifyTemplateVO;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 *
 * @author cl
 * @date 2021-05-20 11:09:53
 */
@RestController("platformNotifyTemplateTagController")
@RequestMapping("/p/notify_template_tag")
@Api(tags = "platform-标签消息")
public class NotifyTemplateTagController {

    @Autowired
    private NotifyTemplateTagService notifyTemplateTagService;
    @Autowired
    private NotifyTemplateService notifyTemplateService;

    @Autowired
	private MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取列表")
	public ServerResponseEntity<PageVO<NotifyTemplateVO>> page(@Valid PageDTO pageDTO) {
		PageVO<NotifyTemplateVO> notifyTemplateTagPage = notifyTemplateTagService.pageTagNotify(pageDTO);
		return ServerResponseEntity.success(notifyTemplateTagPage);
	}

    @GetMapping("/info")
    @ApiOperation(value = "获取", notes = "根据templateId获取")
    public ServerResponseEntity<NotifyTemplateVO> getByTemplateId(@RequestParam Long templateId) {
        return ServerResponseEntity.success(notifyTemplateTagService.getByTemplateId(templateId));
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody NotifyTemplateDTO notifyTemplateDTO) {
        List<Long> tagIds = notifyTemplateDTO.getTagIds();
        if(CollUtil.isEmpty(tagIds)){
            throw new LuckException("请至少选择一个标签！");
        }
        NotifyTemplate notifyTemplate = mapperFacade.map(notifyTemplateDTO, NotifyTemplate.class);
        notifyTemplate.setTemplateId(null);
        notifyTemplate.setStatus(1);
        notifyTemplate.setSendType(SendTypeEnum.CUSTOMIZE.getValue());
        notifyTemplate.setNotifyTypes(NotifyType.APP.value().toString());
        notifyTemplateTagService.saveTagNotify(notifyTemplate,tagIds);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody NotifyTemplateDTO notifyTemplateDTO) {
        if (!Objects.equals(SendTypeEnum.CUSTOMIZE.getValue(), notifyTemplateDTO.getSendType())) {
            throw new LuckException("非自定义类型的消息模板不可以使用此接口更新");
        }
        notifyTemplateTagService.updateTemplateAndTag(notifyTemplateDTO);
        return ServerResponseEntity.success();
    }


    @DeleteMapping
    @ApiOperation(value = "删除", notes = "根据id删除")
    public ServerResponseEntity<Void> delete(@RequestParam Long templateId) {
        NotifyTemplateVO notifyTemplateVO = notifyTemplateService.getByTemplateId(templateId);
        if (!Objects.equals(SendTypeEnum.CUSTOMIZE.getValue(), notifyTemplateVO.getSendType())) {
            throw new LuckException("非自定义类型的消息模板不可以使用此接口更新");
        }
        notifyTemplateTagService.deleteTemplateTagByTempLateId(templateId);
        return ServerResponseEntity.success();
    }


    @PutMapping("/send_msg" )
    @ApiOperation(value = "给标签用户推送模板的站内消息消息", notes = "给标签用户推送模板的站内消息")
    public ServerResponseEntity<Void> sendMsg(@RequestParam Long templateId) {
        notifyTemplateTagService.sendMsg(templateId);
	    return ServerResponseEntity.success();
    }

}
