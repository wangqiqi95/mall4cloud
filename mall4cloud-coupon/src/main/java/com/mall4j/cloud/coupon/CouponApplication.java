package com.mall4j.cloud.coupon;

//import com.mall4j.cloud.common.limiter.anns.EnableRedisRateLimit;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author FrozenWatermelon
 * @date 2020/11/19
 */
@SpringBootApplication(scanBasePackages = { "com.mall4j.cloud" },exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.mall4j.cloud.**.feign"})
//@EnableHystrix
//@EnableRedisRateLimit
public class CouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}
}
