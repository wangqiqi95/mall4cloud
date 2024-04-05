package com.mall4j.cloud.group.controller.questionnaire.admin;

import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.questionnaire.*;
import com.mall4j.cloud.group.model.Questionnaire;
import com.mall4j.cloud.group.service.QuestionnaireExcelService;
import com.mall4j.cloud.group.service.QuestionnaireFormService;
import com.mall4j.cloud.group.service.QuestionnaireService;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.vo.questionnaire.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 问卷信息表
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
@RestController("questionnaireController")
@RequestMapping("/mp/questionnaire")
@Api(tags = "问卷管理端接口")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionnaireFormService questionnaireFormService;

    @Autowired
    private QuestionnaireExcelService questionnaireDetailExcelService;

	@GetMapping("/page")
	@ApiOperation(value = "分页获取问卷信息活动列表", notes = "分页获取问卷信息活动列表")
	public ServerResponseEntity<PageVO<QuestionnaireDetailVO>> page(@Valid PageDTO pageDTO, QuestionnairePageDTO questionnairePageDTO) {
		PageVO<QuestionnaireDetailVO> questionnairePage = questionnaireService.page(pageDTO,questionnairePageDTO);
		return ServerResponseEntity.success(questionnairePage);
	}

	@GetMapping
    @ApiOperation(value = "获取单个问卷信息活动详情", notes = "获取单个问卷信息活动详情")
    public ServerResponseEntity<QuestionnaireDetailVO> getById(@RequestParam Long id) {
        return ServerResponseEntity.success(questionnaireService.getDetailById(id));
    }


    @GetMapping("/answerPage")
    @ApiOperation(value = "分页获取用户答卷信息表列表", notes = "分页获取用户答卷信息表列表")
    public ServerResponseEntity<PageVO<QuestionnaireUserAnswerRecordPageVO>> answerPage(@Valid PageDTO pageDTO, AnswerPageDTO answerPageDTO) {
        PageVO<QuestionnaireUserAnswerRecordPageVO> questionnairePage = questionnaireService.answerPage(pageDTO,answerPageDTO);
        return ServerResponseEntity.success(questionnairePage);
    }

    @PostMapping
    @ApiOperation(value = "保存问卷信息活动", notes = "保存问卷信息活动")
    public ServerResponseEntity<Questionnaire> save(@Valid @RequestBody QuestionnaireDTO questionnaireDTO) {
        return ServerResponseEntity.success(questionnaireService.save(questionnaireDTO));
    }

    @PutMapping
    @ApiOperation(value = "更新问卷信息活动", notes = "更新问卷信息活动")
    public ServerResponseEntity<Void> update(@Valid @RequestBody QuestionnaireDTO questionnaireDTO) {
        questionnaireService.update(questionnaireDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Long id){
        questionnaireService.enable(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Long id){
        questionnaireService.disable(id);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除问卷信息活动", notes = "删除问卷信息活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        questionnaireService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("/export/{id}")
    @ApiOperation(value = "根据活动Id导出问卷记录", notes = "根据活动Id导出问卷记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "isSubmit", value = "是否已提交", required = true, dataType = "boolean", paramType = "query")
    })
    public ServerResponseEntity<Void> export(@PathVariable Long id,
                                             @RequestParam(value = "isSubmit", defaultValue = "true")Boolean isSubmit) {
        questionnaireFormService.exportExcel(id, isSubmit);
        return ServerResponseEntity.success();
    }

    @PostMapping("/resolve_excel")
    @ApiOperation(value = "解析excel并返回缓存key", notes = "解析excel并返回缓存key")
    public ServerResponseEntity<QuestionnaireResolveExcelVO> resolveExcel(MultipartFile file) throws IOException {
        QuestionnaireResolveExcelVO vo = questionnaireDetailExcelService.resolveExcelAndSetRedis(file.getInputStream());
        return ServerResponseEntity.success(vo);
    }

    @GetMapping("/un_submit")
    @ApiOperation(value = "用户未提交统计", notes = "用户未提交统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID", required = true, dataType = "Long", paramType = "path"),
    })
    public ServerResponseEntity<PageVO<QuestionnaireUserUnSubmitVO>> unSubmitPage(@Valid PageDTO pageDTO, Long activityId){
        return ServerResponseEntity.success(questionnaireService.pageCountUserUnSubmit(pageDTO, activityId));
    }

    @GetMapping("/export_user")
    @ApiOperation(value = "导出用户名单", notes = "导出用户名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "redisKey", value = "缓存Key", required = false, dataType = "String", paramType = "query"),
    })
    public ServerResponseEntity<Void> exportUser(String redisKey, Long activityId, HttpServletResponse response){
        questionnaireService.exportUser(redisKey, activityId, response);
        return ServerResponseEntity.success();
    }

    @GetMapping("/gift_addr")
    @ApiOperation(value = "用户实物奖品地址信息详情", notes = "用户实物奖品地址信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "query")
    })
    public ServerResponseEntity<QuestionnaireUserGiftAddrVO> userGiftAddrInfo(@RequestParam Long activityId,
                                                                              @RequestParam Long userId){
        QuestionnaireUserGiftAddrVO vo = questionnaireService.userGiftAddrInfo(activityId, userId);
        return ServerResponseEntity.success(vo);
    }

    @PostMapping("/deliver_gift")
    @ApiOperation(value = "实物奖品发货", notes = "实物奖品发货")
    public ServerResponseEntity<Void> deliverGift(@RequestBody QuestionnaireUserGiftAddrDTO dto){
        questionnaireService.deliverGift(dto);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update/cache_user")
    @ApiOperation(value = "更新缓存的用户数据", notes = "更新缓存的用户数据")
    public ServerResponseEntity<QuestionnaireResolveExcelVO> updateCacheUser(@RequestBody QuestionnaireUpdateExcelDTO questionnaireUpdateExcelDTO){
        QuestionnaireResolveExcelVO vo = questionnaireService.updateCacheUser(questionnaireUpdateExcelDTO);
        return ServerResponseEntity.success(vo);
    }

    @GetMapping("/page/cache_user")
    @ApiOperation(value = "分页查询缓存用户数据", notes = "分页查询缓存用户数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "redisKey", value = "缓存Key", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "activityId", value = "活动ID", required = false, dataType = "Long", paramType = "query"),
    })
    public ServerResponseEntity<PageVO<QuestionnaireUserExcelVO>> pageCacheUser(String redisKey,
                                                                                @Valid PageDTO pageDTO,
                                                                                String phone,
                                                                                Long activityId){
        PageVO<QuestionnaireUserExcelVO> pageVO =  questionnaireService.pageCacheUser(redisKey, pageDTO, phone, activityId);
        return ServerResponseEntity.success(pageVO);
    }

}
