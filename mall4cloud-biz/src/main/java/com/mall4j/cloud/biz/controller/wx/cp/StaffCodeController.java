package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.api.biz.dto.cp.StaffCodeCreateDTO;
import com.mall4j.cloud.api.biz.dto.cp.StaffCodeRefPDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeIdDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodePageDTO;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeRef;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.StaffCode;
import com.mall4j.cloud.biz.service.cp.StaffCodeRefService;
import com.mall4j.cloud.biz.service.cp.StaffCodeService;
import com.mall4j.cloud.biz.service.cp.WelcomeAttachmentService;
import com.mall4j.cloud.biz.vo.cp.AttachMentVO;
import com.mall4j.cloud.biz.vo.cp.StaffCodeDetailVO;
import com.mall4j.cloud.biz.vo.cp.StaffCodeVO;
import com.mall4j.cloud.biz.wx.cp.constant.CodeType;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StaffCodeOriginEumn;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.wx.cp.utils.ZipUtils;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 员工活码配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformSimpleStaffCodeController")
@RequestMapping("/p/cp/simple/staff_code")
@Api(tags = "员工活码配置-simple版")
public class StaffCodeController {

    private final StaffCodeService staffCodeService;
    private final WelcomeAttachmentService attachmentService;
    private final StaffCodeRefService staffCodeRefService;
	private final MapperFacade mapperFacade;
    private final StoreFeignClient storeFeignClient;
    private final StaffFeignClient staffClient;

	@GetMapping("/page")
	@ApiOperation(value = "获取员工活码表列表", notes = "分页获取员工活码表列表")
	public ServerResponseEntity<PageVO<StaffCodeVO>> page(@Valid PageDTO pageDTO, StaffCodePageDTO request) {
		PageVO<StaffCodeVO> staffCodeVOPageVO = staffCodeService.page(pageDTO,request);
		return ServerResponseEntity.success(staffCodeVOPageVO);
	}

	@GetMapping
    @ApiOperation(value = "获取员工活码表", notes = "根据id获取员工活码表")
    public ServerResponseEntity<StaffCodeDetailVO> getById(@RequestParam Long id) {
        StaffCode staffCode = staffCodeService.getById(id);
        CpWelcomeAttachment attachments = attachmentService.getAttachmentByWelId(id, OriginType.STAFF_CODE.getCode());
        List<CpStaffCodeRef> staffs = staffCodeRefService.listByCodeId(id);
        return ServerResponseEntity.success(new StaffCodeDetailVO(staffCode,attachments,staffs));
    }


    @PostMapping
    @ApiOperation(value = "保存员工活码表", notes = "保存员工活码表")
    public ServerResponseEntity<Void> save(@Valid @RequestBody StaffCodeDTO request) {
        StaffCode staffCode = mapperFacade.map(request, StaffCode.class);
        staffCode.setCreateBy(AuthUserContext.get().getUserId());
        staffCode.setStatus(StatusType.YX.getCode());
        if(!CollectionUtils.isEmpty(request.getTagList())) {
            staffCode.setTags(Json.toJsonString(request.getTagList()));
        }
        staffCode.setCreateName(AuthUserContext.get().getUsername());
        staffCode.setFlag(FlagEunm.USE.getCode());
        staffCode.setCreateTime(new Date());
        staffCode.setUpdateTime(staffCode.getCreateTime());
        staffCode.setOrigin(StaffCodeOriginEumn.BACK.getCode());
        AttachmentExtDTO attachMent = AttachMentVO.getAttachMent(request);
        staffCodeService.save(staffCode,attachMent,request.getStaffList());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新员工活码表", notes = "更新员工活码表")
    public ServerResponseEntity<Void> update(@Valid @RequestBody StaffCodeDTO request) {
        StaffCode staffCode = mapperFacade.map(request, StaffCode.class);
        staffCode.setUpdateTime(new Date());
        if(!CollectionUtils.isEmpty(request.getTagList())) {
            staffCode.setTags(Json.toJsonString(request.getTagList()));
        }
        AttachmentExtDTO attachMent = AttachMentVO.getAttachMent(request);
        staffCodeService.update(staffCode,attachMent,request.getStaffList());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除员工活码表", notes = "根据员工活码表id删除员工活码表")
    public ServerResponseEntity<Void> delete(@RequestParam Long[] ids) {
	    for(Long id :ids) {
            staffCodeService.deleteById(id);
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/download/batchQrcode")
    @ApiOperation(value = "批量下载员工活码", notes = "批量下载员工活码")
    public ServerResponseEntity<Void> downloadBatchQrcode(@RequestBody StaffCodeIdDTO staffCodeIdDTO, HttpServletResponse response) throws IOException {
        if (staffCodeIdDTO == null || staffCodeIdDTO .getIds() == null || staffCodeIdDTO.getIds().length == 0){
            return ServerResponseEntity.success();
        }
        List<File> fileList = new ArrayList<>();
        for(Long id :staffCodeIdDTO.getIds()){
            StaffCode staffCode = staffCodeService.getById(id);
            List<CpStaffCodeRef> list = staffCodeRefService.listByCodeId(staffCode.getId());
            StringBuffer name = new StringBuffer();
            list.forEach(item->{name.append(item.getStaffName());name.append("-");});
            File file ;
            if(CodeType.SINGLE.getCode()==staffCode.getCodeType()|| CodeType.BATCH.getCode()==staffCode.getCodeType()){
                Long staffId  = list.get(0).getStaffId();
                ServerResponseEntity<StaffVO> staffResponse = staffClient.getStaffById(staffId);
                StaffVO staffVO = staffResponse.getData();
                StoreVO storeVO=storeFeignClient.findByStoreId(staffResponse.getData().getStoreId());
                file = ZipUtils.creatTempImageFile(storeVO,staffVO,staffCode.getQrCode());
            }else{
                file= ZipUtils.creatTempImageFile(name.toString(),staffCode.getQrCode());
            }
            fileList.add(file);
        }
        ZipUtils.downloadZip("qrCode",fileList,response);
        return ServerResponseEntity.success();
    }


    @GetMapping("/download/qrcode")
    @ApiOperation(value = "下载员工活码", notes = "下载员工活码")
    public ServerResponseEntity<Void> downloadQrcode(@RequestParam Long id, HttpServletResponse response) throws IOException, WxErrorException {
       StaffCode staffCode = staffCodeService.getById(id);
       List<CpStaffCodeRef> list = staffCodeRefService.listByCodeId(staffCode.getId());
       StringBuffer name = new StringBuffer();
       list.forEach(item -> {
           name.append(item.getStaffName());
           name.append("-");
       });
       if(CodeType.SINGLE.getCode()==staffCode.getCodeType()||CodeType.BATCH.getCode()==staffCode.getCodeType()){
           Long staffId  = list.get(0).getStaffId();
           ServerResponseEntity<StaffVO> staffResponse = staffClient.getStaffById(staffId);
           StaffVO staffVO = staffResponse.getData();
           StoreVO storeVO=storeFeignClient.findByStoreId(staffResponse.getData().getStoreId());
           ZipUtils.downLoadImage(storeVO,staffVO,staffCode.getQrCode(),response);
       }else {
           ZipUtils.downLoadImage(name.toString(), staffCode.getQrCode(), response);
       }
       return ServerResponseEntity.success();
    }


    @PostMapping("/syncStaffCodeSUP")
    @ApiOperation(value = "外部触发生成员工活动", notes = "外部触发生成员工活动")
    public ServerResponseEntity<Void> syncStaffCodeSUP(@RequestBody List<StaffCodeRefPDTO> refPDTOS, Integer fromType) {
        staffCodeService.syncStaffCodeSUP(new StaffCodeCreateDTO(AuthUserContext.get().getUserId(),
                AuthUserContext.get().getUsername(),0,refPDTOS));
        return ServerResponseEntity.success();
    }


}
