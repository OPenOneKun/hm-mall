package com.hmall.item.web;

import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;

    /**
     * 分页查询所有商品
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public PageDTO<Item> itemPage(int page,int size){
      return itemService.selectItemPage(page,size);
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Item selectById(@PathVariable Long id){
        return itemService.selectItemById(id);
    }

    /**
     * 添加商品
     */
    @PostMapping
    public void addItem(@RequestBody Item item){
      itemService.addItem(item);
    }

    /**
     * 修改商品上架、下架状态
     * @param id
     * @param item
     */
    @PutMapping("/status/{id}/{status}")
    public void updateStatus(@PathVariable Long id,Item item){

        itemService.updateStatus(id,item);
    }

    /**
     * 修改商品
     * @param item
     */
    @PutMapping
    public void updateItem(@RequestBody Item item){
        itemService.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);

    }
}
