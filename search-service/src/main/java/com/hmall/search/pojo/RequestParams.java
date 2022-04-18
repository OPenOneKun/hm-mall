package com.hmall.search.pojo;

import lombok.Data;

@Data
public class RequestParams {
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String category;
    private String brand;
    private Integer minPrice;
    private Integer maxPrice;

}
