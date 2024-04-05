package com.mall4j.cloud.biz.service.live;


import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintDetailRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintListRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.complaint.ComplaintUploadMaterialRequest;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintListResponse;

public interface ComplaintService {

    ComplaintListResponse list(ComplaintListRequest request);

    ComplaintDetailResponse detail(ComplaintDetailRequest request);

    BaseResponse uploadMaterial(ComplaintUploadMaterialRequest request);
}
