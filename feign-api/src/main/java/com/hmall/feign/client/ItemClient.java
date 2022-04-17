package com.hmall.feign.client;

import com.hmall.common.dto.PageDTO;
import com.hmall.feign.config.DefaultFeignConfiguration;
import com.hmall.feign.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "itemservice",configuration = DefaultFeignConfiguration.class)//局部日志配置
public interface ItemClient {
    @GetMapping("/item/list")
    PageDTO<Item> itemPage(@RequestParam("page") int page,@RequestParam("size") int size);
}
