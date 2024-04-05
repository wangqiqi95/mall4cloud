package com.mall4j.cloud.user.service.async;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.api.biz.dto.MultipartFileDto;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExcelMarkingUserPageBO;
import com.mall4j.cloud.user.bo.UserTagRelationBO;
import com.mall4j.cloud.user.dto.ExportUserTagRelationDTO;
import com.mall4j.cloud.user.dto.QueryMarkingUserPageDTO;
import com.mall4j.cloud.user.vo.MarkingUserPageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface AsyncUserTagRelationService{

    void asyncImport(ExportUserTagRelationDTO exportUserTagRelationDTO, List<UserTagRelationBO> userTagRelationBOList,
                     Long createUserId);


    void asyncExport(Long tagId, FinishDownLoadDTO finishDownLoadDTO);
}
