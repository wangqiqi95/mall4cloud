package com.mall4j.cloud.biz.controller.wx.cp;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.mall4j.cloud.biz.dto.chat.KeywordHitDTO;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.task.CpCustGroupTask;
import com.mall4j.cloud.biz.vo.chat.KeywordHitVO;
import com.mall4j.cloud.biz.vo.chat.SoldKeywordHitVO;
import com.mall4j.cloud.biz.vo.cp.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客群配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformCustGroupController")
@RequestMapping("/p/cp/cust_group")
@Api(tags = "客群配置表")
public class CustGroupController {
    private final WxCpService wxCpService;
    private final CustGroupService groupService;
    private final GroupCustInfoService groupCustInfoService;
    private final CpCustGroupTask groupTask;
    private final MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "客群列表接口", notes = "客群列表接口")
    public ServerResponseEntity<PageVO<CustGroupVO>> page(@Valid PageDTO pageDTO, CustGroupDTO request) {
        PageVO<CustGroupVO> groupPage = groupService.page(pageDTO,request);
        return ServerResponseEntity.success(groupPage);
    }

	@GetMapping("/count")
	@ApiOperation(value = "获取客群数及客户数", notes = "获取客群数及客户数")
	public ServerResponseEntity<CustGroupCountVO> count(@Valid  CustGroupDTO request) {
        return ServerResponseEntity.success(groupService.count(request));
	}


    @GetMapping("/refreshCustGroupStatus")
    @ApiOperation(value = "企微客群表跟进人状态更新", notes = "企微客群表跟进人状态更新")
    public ServerResponseEntity<Void> refreshCustGroupStatus() {
        groupTask.refreshCustGroupStatus();
        return ServerResponseEntity.success();
    }


    @PostMapping("/addTag")
    @ApiOperation(value = "添加标签接口", notes = "添加标签接口")
    public ServerResponseEntity<Void> addTag(@Valid @RequestBody AddTagDTO request) {
        groupService.addTag(request.getId(),request.getTagIds());
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "客群详情", notes = "客群详情")
    public ServerResponseEntity<CustGroupDetailDTO> getById(@RequestParam String id) {
        CpCustGroup custGroup =groupService.getById(id);
        if(Objects.isNull(custGroup)){
            return ServerResponseEntity.success();
        }
//        List<Tag> tagList = new ArrayList<>();
//        List<GroupTagRef> groupTagRefs = tagRefService.getByGroupId(id);
//        if(!CollectionUtils.isEmpty(groupTagRefs)){
//            groupTagRefs.forEach(groupTagRef ->tagList.add(tagService.getById(groupTagRef.getTagId())));
//        }
        CustGroupVO custGroupVO=mapperFacade.map(custGroup,CustGroupVO.class);
        return ServerResponseEntity.success(new CustGroupDetailDTO(custGroupVO,null));
    }

    @PostMapping("/addQrCode")
    @ApiOperation(value = "添加群二维码接口", notes = "添加群二维码接口")
    public ServerResponseEntity<Void> addQrCode(@Valid @RequestBody AddQrCodeDTO request) {
        CpCustGroup custGroup = groupService.getById(request.getId());
        custGroup.setQrCode(request.getQrCode());
        custGroup.setExpireDate(request.getExpireDate());
        groupService.updateById(custGroup);
        return ServerResponseEntity.success();
    }

    @GetMapping("/pageQueryCustInfo")
    @ApiOperation(value = "客群详情-成员", notes = "客群详情-成员")
    public ServerResponseEntity<PageVO<CpGroupCustInfoVO>> pageQueryCustInfo(@Valid PageDTO pageDTO, PageQueryCustInfo request) {
        return ServerResponseEntity.success(groupCustInfoService.page(pageDTO,request));
    }

    @GetMapping("/soldGroupExcel")
    @ApiOperation(value = "客群导出", notes = "客群导出")
    public void soldGroupExcel(CustGroupDTO dto, HttpServletResponse response) {
        List<SoldGroupCustVO> dataList=groupService.soldList(dto);
        String fileName="群信息-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            ExcelUtil.exportExcel(SoldGroupCustVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/custInfo/soldExcel")
    @ApiOperation(value = "客群详情-导出", notes = "客群详情-导出")
    public void soldExcel(PageQueryCustInfo dto, HttpServletResponse response) {
        List<CpGroupCustInfoVO> list=groupCustInfoService.soldExcel(dto);
        String fileName="custInfo-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            List<SoldGroupCustInfoVO> dataList= mapperFacade.mapAsList(list, SoldGroupCustInfoVO.class);
            for (SoldGroupCustInfoVO soldGroupCustInfoVO : dataList) {
                if(StrUtil.isNotEmpty(soldGroupCustInfoVO.getPhone()) && soldGroupCustInfoVO.getPhone().startsWith("[")){
                    List<String> phones= JSONArray.parseArray(soldGroupCustInfoVO.getPhone(),String.class);
                    if(CollUtil.isNotEmpty(phones)){
                        soldGroupCustInfoVO.setPhone(phones.stream().collect(Collectors.joining(",")));
                    }
                }
            }
            ExcelUtil.exportExcel(SoldGroupCustInfoVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }


    @GetMapping("/sendMsg")
    @ApiOperation(value = "发送消息", notes = "发送消息")
    public ServerResponseEntity<Void> sendMsg() throws WxErrorException {

        WxCpMessage wxCpMessage =WxCpMessage.TEXTCARD()
                .title("未添加好友提醒")
                .url("http://baidu.com")
                .agentId(1000003)
                .toUser("18038855020")
                .btnTxt("点击查看未添加企微好友的会员")
                .description("<div class=\\\"gray\\\">2022年02月28日</div> <div class=\\\"normal\\\">仍有5位会员还未添加企微好友，快去添加吧~</div>")
                .build();
        wxCpService.getMessageService().send(wxCpMessage);

        return ServerResponseEntity.success();
    }





}
