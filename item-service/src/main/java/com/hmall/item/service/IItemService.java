package com.hmall.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;

public interface IItemService extends IService<Item> {
    PageDTO<Item> selectItemPage(int page, int size);

    Item selectItemById(Long id);

    void addItem(Item item);

    void updateStatus(Long id, Item item);

    void updateItem(Item item);

    void deleteItem(Long id);
}
