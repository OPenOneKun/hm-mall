package com.hmall.feign.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Item {
    private Long id;//商品id
    private String name;//商品名称
    private Long price;//价格（分）
    private Integer stock;//库存数量
    private String image;//商品图片
    private String category;//分类名称
    private String brand;//品牌名称
    private String spec;//规格
    private Integer sold;//销量
    private Integer commentCount;//评论数
    private Integer status;//商品状态 1-正常，2-下架
    private Boolean isAD;//商品状态 1-正常，2-下架
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
