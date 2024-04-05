package com.mall4j.cloud.biz.controller.admin;


import com.mall4j.cloud.biz.dto.AttachFileDTO;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.service.AttachFileService;
import com.mall4j.cloud.biz.vo.AttachFileVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 上传文件记录表
 *
 * @author YXF
 * @date 2020-11-21 10:21:40
 */
@RestController("adminAttachFileController")
@RequestMapping("/mp/attach_file")
@Api(tags = "admin-上传文件记录表")
public class AttachFileController {

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "获取上传文件记录表列表", notes = "分页获取上传文件记录表列表")
    public ServerResponseEntity<PageVO<AttachFileVO>> page(@Valid PageDTO pageDTO, AttachFileDTO attachFileDTO) {
        if (Objects.equals(attachFileDTO.getAttachFileGroupId(), 0L)) {
            attachFileDTO.setAttachFileGroupId(null);
        }
        PageVO<AttachFileVO> attachFilePage = attachFileService.page(pageDTO, attachFileDTO);
        return ServerResponseEntity.success(attachFilePage);
    }

    @PostMapping
    @ApiOperation(value = "保存上传文件记录", notes = "保存上传文件记录")
    public ServerResponseEntity<Void> save(@RequestBody List<AttachFileDTO> attachFileDtos) {
        List<AttachFile> attachFiles = mapperFacade.mapAsList(attachFileDtos, AttachFile.class);
        attachFileService.save(attachFiles);
        return ServerResponseEntity.success();
    }

    /**
     * 更改文件名或分组
     */
    @PutMapping("/update_file")
    @ApiOperation(value = "更新文件记录", notes = "更新文件记录")
    public ServerResponseEntity<Boolean> updateFileName(@RequestBody AttachFileDTO attachFileDto) {
        if (Objects.isNull(attachFileDto.getFileName())) {
            // 文件名称不能为空
            throw new LuckException("文件名称不能为空");
        }
        AttachFile attachFile = mapperFacade.map(attachFileDto, AttachFile.class);
        return ServerResponseEntity.success(attachFileService.updateFileName(attachFile));
    }

    @DeleteMapping
    @ApiOperation(value = "删除上传文件记录", notes = "根据上传文件记录表id删除上传文件记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long fileId) {
        attachFileService.deleteById(fileId);
        return ServerResponseEntity.success();
    }

}
