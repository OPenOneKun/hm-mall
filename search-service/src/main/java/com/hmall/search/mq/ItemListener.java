package com.hmall.search.mq;


import com.hmall.feign.client.ItemClient;
import com.hmall.feign.pojo.Item;
import com.hmall.search.constatnts.MqConstants;
import com.hmall.search.service.SearchService;
import com.hmall.search.service.impl.SearchServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {


@Autowired
private SearchService searchService;


    /**
     * 监听商品新增或修改的业务
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.item_INSERT_QUEUE)
    public void listenHotelInsertOrUpdate(Long id){
       searchService.insertById(id);
    }

    /**
     * 监听商品删除，上下架的业务
     * @param id 酒店id
     */
    @RabbitListener(queues = MqConstants.item_DELETE_QUEUE)
    public void listenHotelDelete(Long id){
        searchService.deleteById(id);
    }
}