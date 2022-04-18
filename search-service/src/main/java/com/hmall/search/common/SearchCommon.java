package com.hmall.search.common;

import com.alibaba.fastjson.JSON;
import com.hmall.common.dto.PageDTO;
import com.hmall.search.pojo.ItemDoc;
import com.hmall.search.pojo.RequestParams;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Map;

public class SearchCommon {
    //处理响应
    public static PageDTO<ItemDoc> handleResponse(SearchResponse response) {
        //解析响应结果
        SearchHits searchHits = response.getHits();
        //创建集合接收返回的数据
        ArrayList<ItemDoc> itemDocs = new ArrayList<>();
        //获取结果总数
        long total = searchHits.getTotalHits().value;

        //获取结果集
        SearchHit[] hits = searchHits.getHits();

        for (SearchHit hit : hits) {
            //获取结果
            String json = hit.getSourceAsString();

            //反序列化
            ItemDoc itemDoc = JSON.parseObject(json, ItemDoc.class);
            //获取高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //判断结果是否空
            if(!CollectionUtils.isEmpty(highlightFields)){
                //获取结果
                HighlightField highlightField = highlightFields.get("name");
                //判断是否为空
                if(highlightField!=null){
                    //获取高亮内容
                    String name = highlightField.fragments()[0].toString();
                    //替换普通内容
                    itemDoc.setName(name);
                }


            }
            //添加到集合
            itemDocs.add(itemDoc);
        }

        return new PageDTO(total,itemDocs);
    }


    //条件构造方法
    public static void buildBasicQuery(RequestParams params, SearchRequest request) {
        //DSL语句bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //搜索框
        if (params.getKey() == null || "".equals(params.getKey())) {
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        } else {
            boolQueryBuilder.must(QueryBuilders.matchQuery("all", params.getKey()));
            request.source().highlighter(new HighlightBuilder().field("name")
                    //是否要与查询字段匹配
                    .requireFieldMatch(false));
        }


        //分类
        if(params.getCategory()!=null && !"".equals(params.getCategory())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("category", params.getCategory()));
        }

        //品牌
        if(params.getBrand()!=null && !"".equals(params.getBrand())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", params.getBrand()));
        }



        //价格
        if (params.getMinPrice() != null && params.getMaxPrice() != null) {
            boolQueryBuilder.filter(QueryBuilders
                    .rangeQuery("price")
                    .gte(params.getMinPrice())
                    .lte(params.getMaxPrice())
            );
        }


        //放入source
        request.source().query(boolQueryBuilder);
    }
}
