package com.hmall.search.service;

import com.hmall.common.dto.PageDTO;
import com.hmall.search.pojo.ItemDoc;
import com.hmall.search.pojo.RequestParams;

import java.util.List;
import java.util.Map;

public interface SearchService {
    List<String> searchSuggestion(String key);

    Map<String, List<String>> filters(RequestParams params);

    PageDTO<ItemDoc> searchAll(RequestParams params);

    void deleteById(Long id);

    void insertById(Long id);

}
