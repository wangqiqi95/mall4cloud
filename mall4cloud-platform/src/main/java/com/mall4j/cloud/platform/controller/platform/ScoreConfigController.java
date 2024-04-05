
package com.mall4j.cloud.platform.controller.platform;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.common.constant.ConfigNameConstant;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.platform.dto.ScoreConfigDTO;
import com.mall4j.cloud.platform.model.SysConfig;
import com.mall4j.cloud.platform.service.SysConfigService;
import com.mall4j.cloud.platform.vo.ScoreConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 积分基本配置信息
 * @author lhd
 */
@RestController
@RequestMapping("/p/score_config")
public class ScoreConfigController {

	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 获取配置信息
	 */
	@GetMapping("/info/{key}")
	public ServerResponseEntity<ScoreConfigVO> info(@PathVariable("key") String key){
		ScoreConfigVO scoreConfig = sysConfigService.getSysConfigObject(key,ScoreConfigVO.class);
		if(Objects.isNull(scoreConfig)){
			return ServerResponseEntity.success(new ScoreConfigVO());
		}
		if(scoreConfig.getSignInScoreString() != null) {
			List<Integer> signScore = new ArrayList<>();
			String[] scores = scoreConfig.getSignInScoreString().split(StrUtil.COMMA);
			for (String score : scores) {
				signScore.add(Integer.valueOf(score.trim()));
			}
			scoreConfig.setSignInScore(signScore);
		}
		return ServerResponseEntity.success(scoreConfig);
	}


	/**
	 * 保存配置
	 */
	@PostMapping
	public ServerResponseEntity<Void> save(@RequestBody @Valid ScoreConfigDTO scoreConfig){
		SysConfig config = new SysConfig();
		List<Integer> signInScore = scoreConfig.getSignInScore();
		if(CollectionUtil.isNotEmpty(signInScore)) {
			StringBuilder signsString = new StringBuilder();
			for (Integer score : signInScore) {
				signsString.append(score).append(StrUtil.COMMA);
			}
			signsString.deleteCharAt(signsString.length() - 1);
			scoreConfig.setSignInScoreString(signsString.toString());
			scoreConfig.setSignInScore(null);
		}
		String paramValue = Json.toJsonString(scoreConfig);
		config.setParamKey(ConfigNameConstant.SCORE_CONFIG);
		config.setParamValue(paramValue);
		config.setRemark(ConfigNameConstant.SCORE_REMARKS);
		sysConfigService.saveOrUpdateSysConfig(config);
		return ServerResponseEntity.success();
	}


}
