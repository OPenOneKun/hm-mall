package com.hmall.feign.client;

import com.hmall.feign.config.DefaultFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "userservice",configuration = DefaultFeignConfiguration.class)//局部日志配置
public interface UserClient {
}
