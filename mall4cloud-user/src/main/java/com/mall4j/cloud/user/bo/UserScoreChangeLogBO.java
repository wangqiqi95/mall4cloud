package com.mall4j.cloud.user.bo;

import com.mall4j.cloud.user.model.UserScoreGetLog;
import com.mall4j.cloud.user.model.UserScoreLog;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用户积分日志、积分明细
 * @author cl
 * @date 2021-06-17 17:46:07
 */
public class UserScoreChangeLogBO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户积分日志集合")
    private List<UserScoreLog> userScoreLogs;

    @ApiModelProperty("用户积分明细集合-保存")
    private List<UserScoreGetLog> saveUserScoreGetLogs;

    @ApiModelProperty("用户积分明细集合-修改")
    private List<UserScoreGetLog> updateUserScoreGetLogs;

    public List<UserScoreLog> getUserScoreLogs() {
        return userScoreLogs;
    }

    public void setUserScoreLogs(List<UserScoreLog> userScoreLogs) {
        this.userScoreLogs = userScoreLogs;
    }

    public List<UserScoreGetLog> getSaveUserScoreGetLogs() {
        return saveUserScoreGetLogs;
    }

    public void setSaveUserScoreGetLogs(List<UserScoreGetLog> saveUserScoreGetLogs) {
        this.saveUserScoreGetLogs = saveUserScoreGetLogs;
    }

    public List<UserScoreGetLog> getUpdateUserScoreGetLogs() {
        return updateUserScoreGetLogs;
    }

    public void setUpdateUserScoreGetLogs(List<UserScoreGetLog> updateUserScoreGetLogs) {
        this.updateUserScoreGetLogs = updateUserScoreGetLogs;
    }

    @Override
    public String toString() {
        return "UserScoreChangeLogBO{" +
                "userScoreLogs=" + userScoreLogs +
                ", saveUserScoreGetLogs=" + saveUserScoreGetLogs +
                ", updateUserScoreGetLogs=" + updateUserScoreGetLogs +
                '}';
    }
}
