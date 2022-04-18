package com.hmall.search.config;


import com.hmall.search.constatnts.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 声明队列交换机
 */
@Configuration
public class MqConfig {
    /**
     * 配置交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MqConstants.item_EXCHANGE, true, false);
    }

    /**
     * 添加队列在添加商品时
     * @return
     */
    @Bean
    public Queue insertQueue(){
        return new Queue(MqConstants.item_INSERT_QUEUE, true);
    }

    /**
     * 添加队列在删除商品时
     * @return
     */
    @Bean
    public Queue deleteQueue(){
        return new Queue(MqConstants.item_DELETE_QUEUE, true);
    }

    /**
     * 队列绑定交换机
     * @return
     */
    @Bean
    public Binding insertQueueBinding(){
        return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(MqConstants.item_INSERT_KEY);
    }

    /**
     * 队列绑定交换机
     * @return
     */
    @Bean
    public Binding deleteQueueBinding(){
        return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(MqConstants.item_DELETE_KEY);
    }
}