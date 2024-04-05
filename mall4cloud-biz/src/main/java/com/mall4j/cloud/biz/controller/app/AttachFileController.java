package com.mall4j.cloud.biz.controller.app;


import com.mall4j.cloud.biz.dto.AttachFileDTO;
import com.mall4j.cloud.biz.model.AttachFile;
import com.mall4j.cloud.biz.service.AttachFileService;
import com.mall4j.cloud.biz.vo.AttachFileVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
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
@RestController("appAttachFileController")
@RequestMapping("/attach_file")
@Api(tags = "app-文件记录表")
public class AttachFileController {

    @Autowired
    private AttachFileService attachFileService;

    @GetMapping("/get_file_by_id")
    @ApiOperation(value = "根据文件id获取文件信息")
    public ServerResponseEntity<AttachFileVO> getFileById(Long fileId){
        AttachFileVO attachFileVO = attachFileService.getById(fileId);
        return ServerResponseEntity.success(attachFileVO);
    }

}
