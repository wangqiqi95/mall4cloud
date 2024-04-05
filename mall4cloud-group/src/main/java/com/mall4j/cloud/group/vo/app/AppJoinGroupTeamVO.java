package com.mall4j.cloud.group.vo.app;


/**
 * @author YXF
 * @date 2021/3/26
 */
public class AppJoinGroupTeamVO {

    /**
     * 当前用户参团的团队ID（null表示还没有参加该活动）
     */
    private Long joinGroupTeamId;
    /**
     * 已交易数量
     */
    private Integer prodCount;

    public Long getJoinGroupTeamId() {
        return joinGroupTeamId;
    }

    public void setJoinGroupTeamId(Long joinGroupTeamId) {
        this.joinGroupTeamId = joinGroupTeamId;
    }

    public Integer getProdCount() {
        return prodCount;
    }

    public void setProdCount(Integer prodCount) {
        this.prodCount = prodCount;
    }

    @Override
    public String toString() {
        return "AppJoinGroupTeamVO{" +
                "joinGroupTeamId=" + joinGroupTeamId +
                ", prodCount=" + prodCount +
                '}';
    }
}
