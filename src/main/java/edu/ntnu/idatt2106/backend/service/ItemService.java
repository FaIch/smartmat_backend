package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.ItemEntity;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemService {


    private ItemRepository itemRepository;


    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    //Todo: fix methods when connected backend to database
    public List<ItemEntity> getAllItems() {
        return null;
    }


    public ItemEntity getItem(Long id) {
        return null;
    }


    public List<ItemEntity> getItemByCategory(String category) {
        return null;
    }


    public void saveOrUpdate(ItemEntity itemEntity) {

    }
}
