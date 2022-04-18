package com.hmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hmall.common.dto.PageDTO;
import com.hmall.feign.client.ItemClient;
import com.hmall.feign.pojo.Item;
import com.hmall.search.common.SearchCommon;
import com.hmall.search.pojo.ItemDoc;
import com.hmall.search.pojo.RequestParams;
import com.hmall.search.service.SearchService;
import org.apache.logging.log4j.util.StringBuilders;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ItemClient itemClient;


    //搜索框自动补全
    @Override
    public List<String> searchSuggestion(String key) {
        try {
            //1.准备request
            SearchRequest request = new SearchRequest("item");
            //2.准备DSL
            request.source().suggest(new SuggestBuilder().addSuggestion("suggestions",
                    SuggestBuilders.completionSuggestion("suggestion").skipDuplicates(true).prefix(key).size(10)));
            //3.发起请求

            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //4.解析结果
            Suggest suggest = response.getSuggest();
            //4.1根据补全查询名称，获取补全结果
            CompletionSuggestion suggestion = suggest.getSuggestion("suggestions");

            //4.2获取options
            List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
            //创建集合接收结果
            ArrayList<String> list = new ArrayList<>(options.size());

            //4.3遍历
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().string();
                list.add(text);

            }


            return list;
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * 过滤词条查询
     * @param params
     * @return
     */
    @Override
    public Map<String, List<String>> filters(RequestParams params) {
        try {
            //准备request
            SearchRequest request = new SearchRequest("item");

            //基本DSL搜索
            SearchCommon.buildBasicQuery(params,request);
            //将搜索的文档设为0,文档不参与搜索
            request.source().size(0);
            //聚合查询
            //分类
            request.source().aggregation(AggregationBuilders.terms("category_count").field("category").size(100));
            //品牌
            request.source().aggregation(AggregationBuilders.terms("brand_count").field("brand").size(100));


            //处理响应结果
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //获取聚合查询结果全部数据
            Aggregations aggregations = response.getAggregations();

            //获取聚合数据
            Terms categoryCount = aggregations.get("category_count");
            Terms brandCountount = aggregations.get("brand_count");

            //获取所有结果
            List<? extends Terms.Bucket> categoryCountBuckets = categoryCount.getBuckets();
            List<? extends Terms.Bucket> brandCountountBuckets = brandCountount.getBuckets();


            //创建接收结果集合
            ArrayList<String> brandBuckets = new ArrayList<>(brandCountountBuckets.size());
            ArrayList<String> categoryBuckets = new ArrayList<>(categoryCountBuckets.size());

            //遍历
            for (Terms.Bucket categoryCountBucket : categoryCountBuckets) {
                String key = (String) categoryCountBucket.getKey();
                categoryBuckets.add(key);
            }

            for (Terms.Bucket brandCountountBucket : brandCountountBuckets) {
                String key = (String) brandCountountBucket.getKey();
                 brandBuckets.add(key);
            }

            //创建map集合接收数据
            HashMap<String, List<String>>  map = new HashMap<>();

            map.put("category",categoryBuckets);
            map.put("brand",brandBuckets);


            return map;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    /**
     * 分页搜索所有商品
     * @param params
     * @return
     */
    @Override
    public PageDTO<ItemDoc> searchAll(RequestParams params) {

        try {
            //创建request
            SearchRequest request = new SearchRequest("item");

            //准备DSL
            //基础搜索
            SearchCommon.buildBasicQuery(params,request);

            //分页
            int page= params.getPage();
            int size = params.getSize();
            request.source().from((page-1)*size).size(size);

            //排序
            if(!params.getSortBy().equals("default")){
                if (params.getSortBy().equals("sold")){
                    request.source().sort("sold",SortOrder.DESC);
                }

                if(params.getSortBy().equals("price")){
                    request.source().sort("price", SortOrder.ASC);
                }

            }

            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            return SearchCommon.handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    /**
     * mq消息队列处理删除索引库数据
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        DeleteRequest request = new DeleteRequest("item", id.toString());

        try {
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * mq队列消息处理新增索引库数据
     * @param id
     */
    @Override
    public void insertById(Long id) {
        //远程调用商品根据id查找商品方法
        Item item = itemClient.selectById(id);
        //创建文档对象
        ItemDoc itemDoc = new ItemDoc();
        //拷贝对象
        BeanUtils.copyProperties(item,itemDoc);

        //准备request对象
        IndexRequest request = new IndexRequest("item").id(item.getId().toString());
        //准备Json文档
        request.source(JSON.toJSONString(itemDoc), XContentType.JSON);
        //发送请求
        try {
            client.index(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
