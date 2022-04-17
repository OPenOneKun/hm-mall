package com.hmall.feign.client;

import com.hmall.feign.config.DefaultFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "searchservice",configuration = DefaultFeignConfiguration.class)
public interface SearchClient {
}
