package com.mall4j.cloud.biz.controller.multishop;

import com.mall4j.cloud.biz.constant.FileType;
import com.mall4j.cloud.biz.dto.AttachFileDTO;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.service.AttachFileService;
import com.mall4j.cloud.biz.vo.AttachFileVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @Author lth
 * @Date 2021/6/1 15:20
 */
@RestController("multishopAttachFileController")
@RequestMapping("/m/attach_file")
@Api(tags = "multishop-上传文件记录表")
public class AttachFileController {

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private MapperFacade mapperFacade;

    @DeleteMapping("/delete_by_ids")
    @ApiOperation(value = "根据文件id列表批量删除文件记录", notes = "根据文件id列表批量删除文件记录")
    public ServerResponseEntity<Void> deleteByIds(@RequestBody List<Long> ids) {
        Long shopId = AuthUserContext.get().getTenantId();
        attachFileService.deleteByIdsAndShopId(ids, shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/batch_move")
    @ApiOperation(value = "根据文件id列表与分组id批量移动文件", notes = "根据文件id列表与分组id批量移动文件")
    public ServerResponseEntity<Void> batchMove(@RequestBody List<Long> ids, @RequestParam(value = "groupId", required = false, defaultValue = "") Long groupId) {
        Long shopId = AuthUserContext.get().getTenantId();
        attachFileService.batchMoveByShopIdAndIdsAndGroupId(shopId, ids, groupId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_file_by_id")
    @ApiOperation(value = "根据文件id获取文件信息")
    public ServerResponseEntity<AttachFileVO> getFileById(Long fileId){
        AttachFileVO attachFileVO = attachFileService.getById(fileId);
        Long shopId = AuthUserContext.get().getTenantId();
        if (!Objects.equals(shopId,attachFileVO.getShopId())){
            throw new LuckException("无法查看非本店铺的文件");
        }
        return ServerResponseEntity.success(attachFileVO);
    }

    @PostMapping("/save_pdf_file")
    @ApiOperation(value = "保存商家发票")
    public ServerResponseEntity<AttachFileVO> savePdfFile(@RequestBody AttachFileDTO attachFileDTO){
        AttachFile attachFile = mapperFacade.map(attachFileDTO, AttachFile.class);
        attachFile.setType(FileType.FILE.value());
        attachFile.setShopId(AuthUserContext.get().getTenantId());
        attachFileService.saveFile(attachFile);
        return ServerResponseEntity.success(attachFileService.getById(attachFile.getFileId()));
    }

}
