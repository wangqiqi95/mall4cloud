package com.mall4j.cloud.flow.vo;

import com.mall4j.cloud.api.user.vo.MemberOverviewVO;

import java.util.List;

/**
 * @author lhd
 */
public class MemberSurveyRespVO {

    private MemberOverviewVO memberOverviewModel;

    private List<MemberOverviewVO> memberOverviewModelList;

    public MemberOverviewVO getMemberOverviewModel() {
        return memberOverviewModel;
    }

    public void setMemberOverviewModel(MemberOverviewVO memberOverviewModel) {
        this.memberOverviewModel = memberOverviewModel;
    }

    public List<MemberOverviewVO> getMemberOverviewModelList() {
        return memberOverviewModelList;
    }

    public void setMemberOverviewModelList(List<MemberOverviewVO> memberOverviewModelList) {
        this.memberOverviewModelList = memberOverviewModelList;
    }

    @Override
    public String toString() {
        return "MemberSurveyRespVO{" +
                "memberOverviewModel=" + memberOverviewModel +
                ", memberOverviewModelList=" + memberOverviewModelList +
                '}';
    }
}
