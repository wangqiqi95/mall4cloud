package com.mall4j.cloud.common.security.adapter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;

/**
 * 使用security的防火墙功能，但不使用security的认证授权登录
 * @author FrozenWatermelon
 */
@Component
public class LuckWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors() // We don't need CSRF for token based authentication
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .and()
            .authorizeRequests().antMatchers(
                "/**").permitAll();
    }


}
