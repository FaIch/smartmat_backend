package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import edu.ntnu.idatt2106.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/items")
public class ItemController {

    /**
     * The ItemService instance used to perform business logic and data access operations.
     */
    private final ItemService itemService;

    /**
     * Constructs a new ItemController instance with the given ItemService.
     *
     * @param itemService the ItemService instance to use.
     */
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Returns a list of all items.
     *
     * @return a ResponseEntity containing a list of Item objects and a status code.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Item>> list(){
        return itemService.getAllItems();
    }

    /**
     * Returns the item with the specified ID.
     *
     * @param id the ID of the item to retrieve.
     * @return a ResponseEntity containing the requested Item object and a status code.
     */
    @GetMapping("/list/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemById(id));
    }

    /**
     * Returns a list of all categories.
     *
     * @return a ResponseEntity containing a list of Category objects and a status code.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(Category.values()).toList());
    }

    /**
     * Returns a list of all units.
     *
     * @return a ResponseEntity containing a list of Unit objects and a status code.
     */
    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getUnits(){
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(Unit.values()).toList());
    }

    /**
     * Returns a list of items that belong to the specified category.
     *
     * @param category the name of the category to filter by.
     * @return a ResponseEntity containing a list of Item objects and a status code.
     */
    @GetMapping("/list/category")
    public ResponseEntity<List<Item>> getItemsByCategory(@RequestParam("category") String category){
        return itemService.getItemByCategory(Category.valueOf(category));
    }

    /**
     * Adds a new item or updates an existing one.
     *
     * @param item the Item object to add or update.
     * @return a ResponseEntity containing a message and a status code.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody Item item){
        return itemService.saveOrUpdateItem(item);
    }
}