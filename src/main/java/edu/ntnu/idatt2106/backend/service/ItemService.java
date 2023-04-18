package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.repository.CustomItemRepository;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemService {


    private ItemRepository itemRepository;
    private CustomItemRepository customItemRepository;


    public ItemService(ItemRepository itemRepository, CustomItemRepository customItemRepository) {
        this.itemRepository = itemRepository;
        this.customItemRepository = customItemRepository;
    }

    //Todo: fix methods when connected backend to database
    /**
     * Method getAllItems
     * @return list of all items in database
     */

    public List<Item> getAllItems() {
        return (List<Item>) itemRepository.findAll();
    }

    /**
     * Method getItems
     * @param id long of item
     * @return item entity which has specific id
     */

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

   /* *//**
     * Method getItemByUser
     * @param user entity of specific user which we want to get items of
     * @return list of items of specific user
     *//*

    public List<ItemEntity> getItemByUser(User user) {
        return itemRepository.findAllByUser(user);
    }*/

    /**
     * Method getItemByCategory
     * @param category string of specific category which we want to get items of
     * @return list of items with the specific category
     */

    public List<Item> getItemByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    /**
     * Method saveOrUpdate
     * @param customItem entity of the item which we want to save or update
     */

    public void saveOrUpdate(CustomItem customItem) {
        customItemRepository.save(customItem);
    }

}
