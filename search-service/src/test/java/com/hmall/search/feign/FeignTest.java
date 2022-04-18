package com.hmall.search.feign;

import com.alibaba.fastjson.JSON;
import com.hmall.common.dto.PageDTO;
import com.hmall.feign.client.ItemClient;
import com.hmall.feign.pojo.Item;
import com.hmall.search.pojo.ItemDoc;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {
   @Autowired
   private ItemClient itemClient;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

//    @Test
//    public void testItem() throws IOException {
//        int page =1;
//        while (true) {
//            //分页查询所有数据
//            PageDTO<Item> itemPageDTO = itemClient.itemPage(page, 500);
//
//            //拿到每页数据
//            List<Item> itemList = itemPageDTO.getList();
//
//            if(itemList.size()<=0){
//                break;
//            }
//            //创建BulkRequest
//            BulkRequest bulkRequest = new BulkRequest();
//
//            //遍历每页数据
//            for (Item item : itemList) {
//                //将普通商品类型转换为商品文档类型
//                ItemDoc itemDoc = new ItemDoc(item);
//                //添加到bulk
//                bulkRequest.add(new IndexRequest("item")
//                        .id(item.getId().toString())
//                        .source(JSON.toJSONString(itemDoc), XContentType.JSON)
//                );
//            }
//            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//
//            System.out.println("response="+response);
//            page++;
//        }
//
//    }

    @Test
    public void testItem2() throws IOException {

            //分页查询所有数据
            PageDTO<Item> itemPageDTO = itemClient.itemPage(1, 5);

            //拿到每页数据
            List<Item> itemList = itemPageDTO.getList();


            //创建BulkRequest
            BulkRequest bulkRequest = new BulkRequest();

        Assert.assertEquals(5, itemList.size());
            //遍历每页数据
            for (Item item : itemList) {
                System.out.println(item);
            }

        }

    }

