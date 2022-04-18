package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {
    @Autowired
    private ItemMapper itemMapper;

    /**
     * 分页查询所有商品
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageDTO<Item> selectItemPage(int page, int size) {
        //创建分页构造器
        Page pageInfo = new Page(page, size);
       //创建条件构造器
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
        //添加条件
        lqw.orderByDesc(Item::getUpdateTime);
        Page itemPage = itemMapper.selectPage(pageInfo, lqw);
        //获取总条数
        long total = itemPage.getTotal();
        //获取当前页数据
        List itemPageRecords = itemPage.getRecords();

       //返回结果
        return new PageDTO<Item>(total,itemPageRecords);
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @Override
    public Item selectItemById(Long id) {

        return itemMapper.selectById(id);
    }

    /**
     * 添加商品
     * @param item
     */
    @Override
    @Transactional
    public void addItem(Item item) {

        itemMapper.insert(item);
    }

    /**
     * 修改商品上架、下架状态
     * @param id
     * @param item
     */
    @Override
    @Transactional
    public void updateStatus(Long id, Item item) {
        //创建条件构造器
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
        //id相等的条件
        lqw.eq(Item::getId,id);
        //修改状态
        itemMapper.update(item,lqw);
    }

    /**
     * 修改商品
     * @param item
     */
    @Override
    @Transactional
    public void updateItem(Item item) {
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Item::getId,item.getId());

        itemMapper.update(item,lqw);
    }

    /**
     * 删除商品
     * @param id
     */
    @Override
    @Transactional
    public void deleteItem(Long id) {
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Item::getId,id);

        itemMapper.delete(lqw);
    }
}
