package com.hmall.feign.client;

import com.hmall.feign.config.DefaultFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "itemservice",configuration = DefaultFeignConfiguration.class)//局部日志配置
public interface ItemClient {

}
