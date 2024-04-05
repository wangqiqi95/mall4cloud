package com.mall4j.cloud.common.security.adapter;

import com.mall4j.cloud.common.constant.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author FrozenWatermelon
 * @date 2020/7/16
 */
public class DefaultAuthConfigAdapter implements AuthConfigAdapter {

	static Set<String> set;
	static {
		set = new CopyOnWriteArraySet<>();
		set.add(Auth.CHECK_TOKEN_URI);
		set.add(EXTERNAL_URI);
		set.add(ACTUATOR_URI);
		set.add(FEIGN_INSIDER_URI);
		set.add(DOC_URI);
	}

	private static final Logger logger = LoggerFactory.getLogger(DefaultAuthConfigAdapter.class);

	public DefaultAuthConfigAdapter() {
		logger.info("not implement other AuthConfigAdapter, use DefaultAuthConfigAdapter... all url need auth...");
	}





	@Override
	public List<String> pathPatterns() {
		return Collections.singletonList("/*");
	}

	@Override
	public Set<String> excludePathPatterns(String... paths) {
		if (paths.length == 0) {
			return set;
		}
		set.addAll(Arrays.asList(paths));
		return set;
	}
}
