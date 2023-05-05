package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Method getAllItems
     * @return list of all items in database
     */
    public ResponseEntity<List<Item>> getAllItems() {

        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.findAll());
    }

    /**
     * Method getItemByCategory
     * @param category string of specific category which we want to get items of
     * @return list of items with the specific category
     */
    public  ResponseEntity<List<Item>> getItemByCategory(Category category) {
        return ResponseEntity.status(HttpStatus.OK).body(itemRepository.findByCategory(category));
    }

    /**
     * Method getItemById
     * @param id id of the item we want to get
     * @return item with the specific id
     */
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    /**
     * Method saveOrUpdateItem
     * @param item the item we want to save or update
     * @return string of the status of the operation
     */
    public ResponseEntity<String> saveOrUpdateItem(Item item) {
        itemRepository.save(item);
        return ResponseEntity.ok("Item added");
    }
}
