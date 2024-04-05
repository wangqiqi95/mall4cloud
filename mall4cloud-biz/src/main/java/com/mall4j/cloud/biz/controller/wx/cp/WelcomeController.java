package com.mall4j.cloud.biz.controller.wx.cp;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.MaterialUseRecordPageDTO;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.vo.cp.*;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.model.cp.ShopWelcomeConfig;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.dto.cp.WelcomeDTO;
import com.mall4j.cloud.biz.model.cp.CpWelcome;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 欢迎语配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformWelcomeController")
@RequestMapping("/p/cp/welcome")
@Api(tags = "欢迎语配置表")
public class WelcomeController {

    private final WelcomeService welcomeService;
    private final WelcomeAttachmentService attachmentService;
    private final StoreWelcomeConfigService storeWelcomeConfigService;
	private final MapperFacade mapperFacade;
    private final CpWelcomeUseRecordService cpWelcomeUseRecordService;
    private final CpWelcomeTimeStateService cpWelcomeTimeStateService;

	@GetMapping("/page")
	@ApiOperation(value = "获取欢迎语配置表列表", notes = "分页获取欢迎语配置表列表")
	public ServerResponseEntity<PageVO<WelcomeVO>> page(@Valid PageDTO pageDTO, WelcomeDTO request) {
		PageVO<WelcomeVO> welcomePage = welcomeService.page(pageDTO,request);
		return ServerResponseEntity.success(welcomePage);
	}

	@GetMapping
    @ApiOperation(value = "获取欢迎语配置表", notes = "根据id获取欢迎语配置表")
    public ServerResponseEntity<WelcomeDetailPlusVO> getById(@RequestParam Long id) {
        CpWelcome welcome = welcomeService.getById(id);
        Assert.isNull(welcome,"个人欢迎语数据为空，请检查id是否正确！");

        List<CpWelcomeAttachment> attachments = attachmentService.listByWelId(id, OriginType.WEL_CONFIG.getCode());

        //分时段id为null为默认欢迎语
        List<CpWelcomeAttachment> defaultAttachments =attachments.stream().filter(s->s.getTimeStateId()==null).collect(Collectors.toList());
        List<ShopWelcomeConfig> shopWelcomeConfigs = storeWelcomeConfigService.listByWelId(id);
        WelcomeDetailPlusVO welcomeDetailVO = new WelcomeDetailPlusVO(welcome,defaultAttachments,shopWelcomeConfigs);

        //查询分时段记录
        List<CpWelcomeTimeStateVO> stateVOS = cpWelcomeTimeStateService.listByWellId(id);
        if(CollUtil.isNotEmpty(stateVOS)){
            //所有不为null的分时段附件，根据时段id分组
            Map<Long, List<CpWelcomeAttachment>> timeStateMaps =attachments.stream().filter(s->s.getTimeStateId()!=null).collect(Collectors.groupingBy(CpWelcomeAttachment::getTimeStateId));
            for (CpWelcomeTimeStateVO stateVO : stateVOS) {
                stateVO.setAttachment(timeStateMaps.get(stateVO.getId()));
                stateVO.setSlogan(SlognUtils.parseSlogn(stateVO.getSlogan()));
                if(StrUtil.isNotEmpty(stateVO.getTimeEnd()) && stateVO.getTimeEnd().equals("23:59")){
                    stateVO.setTimeEnd("24:00");
                }
            }
        }
        welcomeDetailVO.setTimeStateVOS(stateVOS);

        return ServerResponseEntity.success(welcomeDetailVO);
    }

    @PostMapping
    @ApiOperation(value = "保存欢迎语配置表", notes = "保存欢迎语配置表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody WelcomeDTO welcomeDTO) {
        CpWelcome welcome = mapperFacade.map(welcomeDTO, CpWelcome.class);
        welcome.setCreateBy(AuthUserContext.get().getUserId());
        welcome.setStatus(StatusType.WX.getCode());
        welcome.setCreateName(AuthUserContext.get().getUsername());
        welcome.setFlag(StatusType.WX.getCode());
        welcome.setCreateTime(new Date());
        List<AttachmentExtDTO> attachMent = AttachMentVO.getAttachMents(welcomeDTO.getAttachMentBaseDTOS());
        welcomeService.createWelcome(welcome,attachMent,welcomeDTO.getShops(),welcomeDTO);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新欢迎语配置表", notes = "更新欢迎语配置表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody WelcomeDTO welcomeDTO) {
        CpWelcome welcome = mapperFacade.map(welcomeDTO, CpWelcome.class);
        welcome.setUpdateTime(new Date());
        List<AttachmentExtDTO> attachMent = AttachMentVO.getAttachMents(welcomeDTO.getAttachMentBaseDTOS());
        welcomeService.createWelcome(welcome,attachMent,welcomeDTO.getShops(),welcomeDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除欢迎语配置表", notes = "根据欢迎语配置表id删除欢迎语配置表")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        welcomeService.deleteById(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Long id){
        welcomeService.enable(id);
        return ServerResponseEntity.success();
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Long id){
        welcomeService.disable(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("/usePage")
    @ApiOperation(value = "分页获取个人欢迎语使用数据列表", notes = "分页获取个人欢迎语使用数据列表")
    public ServerResponseEntity<PageVO<CpWelcomeUseRecordVO>> usePage(@Valid PageDTO pageDTO, MaterialUseRecordPageDTO request) {
        PageVO<CpWelcomeUseRecordVO> materialPage = cpWelcomeUseRecordService.page(pageDTO,request);
        return ServerResponseEntity.success(materialPage);
    }

    @GetMapping("/record/soldUser")
    @ApiOperation(value = "分页获取个人欢迎语使用数据列表-导出", notes = "分页获取个人欢迎语使用数据列表-导出")
    public void soldUser(MaterialUseRecordPageDTO request, HttpServletResponse response) {
        List<CpWelcomeUseRecordVO> list=cpWelcomeUseRecordService.soldUserRecord(request);
//        if(CollUtil.isEmpty(list)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="user-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<CpWelcomeUseRecordSoldVO> dataList= mapperFacade.mapAsList(list,CpWelcomeUseRecordSoldVO.class);
            ExcelUtil.exportExcel(CpWelcomeUseRecordSoldVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("useStatistics")
    @ApiOperation(value = "欢迎语使用报表记录", notes = "欢迎语使用报表记录")
    public ServerResponseEntity<List<MaterialBrowseRecordByDayVO>> useStatistics(@Valid MaterialUseRecordPageDTO request) {
        if(StrUtil.isEmpty(request.getCreateTimeStart())){
            Assert.faild("开始时间不能为空。");
        }
        if(StrUtil.isEmpty(request.getCreateTimeEnd())){
            Assert.faild("结束时间不能为空。");
        }
        List<MaterialBrowseRecordByDayVO> lists = cpWelcomeUseRecordService.useStatistics(request);
        return ServerResponseEntity.success(lists);
    }
}
