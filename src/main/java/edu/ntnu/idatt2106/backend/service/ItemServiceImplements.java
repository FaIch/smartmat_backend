package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.ItemEntity;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;

import java.util.List;

public class ItemServiceImplements implements ItemService{


    private ItemRepository itemRepository;


    public ItemServiceImplements(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    //Todo: fix methods when connected backend to database
    @Override
    public List<ItemEntity> getAllItems() {
        return null;
    }

    @Override
    public ItemEntity getItem(Long id) {
        return null;
    }

    @Override
    public List<ItemEntity> getItemByCategory(String category) {
        return null;
    }

    @Override
    public void saveOrUpdate(ItemEntity itemEntity) {

    }
}
