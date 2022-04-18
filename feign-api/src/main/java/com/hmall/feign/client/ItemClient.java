package com.hmall.feign.client;

import com.hmall.common.dto.PageDTO;
import com.hmall.feign.config.DefaultFeignConfiguration;
import com.hmall.feign.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "itemservice",configuration = DefaultFeignConfiguration.class)//局部日志配置
public interface ItemClient {
    @GetMapping("/item/list")
    PageDTO<Item> itemPage(@RequestParam("page") int page,@RequestParam("size") int size);

    @GetMapping("/item/{id}")
    Item selectById(@PathVariable Long id);

    @PostMapping("/item")
   void addItem(@RequestBody Item item);

    @DeleteMapping("/item/{id}")
   void deleteItem(@PathVariable Long id);
}
