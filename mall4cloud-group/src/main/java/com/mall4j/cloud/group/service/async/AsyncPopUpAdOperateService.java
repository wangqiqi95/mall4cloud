package com.mall4j.cloud.group.service.async;


import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;

import javax.servlet.http.HttpServletResponse;

public interface AsyncPopUpAdOperateService {

    void AsyncExportData(Long adId, FinishDownLoadDTO finishDownLoadDTO, String popUpAdName);


}
