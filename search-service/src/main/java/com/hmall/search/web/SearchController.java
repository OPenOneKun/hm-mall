package com.hmall.search.web;

import com.hmall.common.dto.PageDTO;
import com.hmall.search.pojo.ItemDoc;
import com.hmall.search.pojo.RequestParams;
import com.hmall.search.service.impl.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private SearchServiceImpl searchService;

    /**
     * 搜索框自动补全
     * @param key
     * @return
     */
    @GetMapping("/suggestion")
    public List<String> searchSuggestion(String key){
       return searchService.searchSuggestion(key);

    }

    @PostMapping("/list")
    public PageDTO<ItemDoc> searchAll(@RequestBody RequestParams params){
        return searchService.searchAll(params);
    }

    @PostMapping("/filters")
    public Map<String,List<String>> filters(@RequestBody RequestParams params){

          return searchService.filters(params);
    }
}
