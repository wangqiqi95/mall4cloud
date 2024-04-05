package com.mall4j.cloud.group.strategy.ad.attachment;

import com.mall4j.cloud.group.bo.AttachmentBO;
import com.mall4j.cloud.group.vo.PopUpAdContainerVO;
import com.mall4j.cloud.group.vo.PopUpAdFormQuestionnaireVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionnaireHandler implements AttachmentHandler{


    private static final String PAGE_URL = "/packageA/pages/question-home/question-home?id=";

    @Override
    public void extraction(PopUpAdContainerVO container, AttachmentBO attachmentBO) {
        List<PopUpAdFormQuestionnaireVO> popUpAdFormQuestionnaireVOList = attachmentBO.getPopUpAdAttachmentVOList().stream()
                .map(adAttachmentVO -> {
                    PopUpAdFormQuestionnaireVO popUpAdFormQuestionnaireVO = new PopUpAdFormQuestionnaireVO();

                    popUpAdFormQuestionnaireVO.setPopUpAdID(attachmentBO.getPopUpAdId());
                    popUpAdFormQuestionnaireVO.setLinkType(adAttachmentVO.getLinkType());
                    popUpAdFormQuestionnaireVO.setPicUrl(adAttachmentVO.getMediaUrl());
                    popUpAdFormQuestionnaireVO.setLink(PAGE_URL + adAttachmentVO.getBusinessId());
                    popUpAdFormQuestionnaireVO.setAutoOffSeconds(attachmentBO.getAutoOffSeconds());
                    popUpAdFormQuestionnaireVO.setAttachmentType(attachmentBO.getAttachmentType());

                    return popUpAdFormQuestionnaireVO;
                })
                .collect(Collectors.toList());


        container.getPopUpAdFormQuestionnaireVOList().addAll(popUpAdFormQuestionnaireVOList);
    }
}
