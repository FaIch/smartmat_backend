package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.item.Unit;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/items")
public class ItemController {


    private final ItemService itemService;

    /*
    Constructor
     */
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /*
     Get all items
     */
    @GetMapping("/list")
    public ResponseEntity<List<Item>> list(){
        return itemService.getAllItems();
    }

    /*
    Get item by id
     */
    @GetMapping("/list/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemById(id));
    }

    /*
    Get all categories
     */
   @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(){
      return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(Category.values()).toList());
    }

    /*
    Get all units
     */
    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getUnits(){
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(Unit.values()).toList());
    }

    /*
    Get items by category
     */
    @GetMapping("/list/category")
    public ResponseEntity<List<Item>>  getItemsByCategory(@RequestParam("category") String category){
        System.out.println(category);
        return itemService.getItemByCategory(Category.valueOf(category));
    }

    /*
    Add items
     */
    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody Item item){
        return itemService.saveOrUpdateItem(item);
    }

    //Date must be on form yyyy-mm-dd
    /*
    Add custom items
     */
    @PostMapping("/addCustom")
    public ResponseEntity<?> uploadCustomItem(@RequestParam("name")String name, @RequestParam("weight") double weight,
                                               @RequestParam("category") String category, @RequestParam("date") String date,
                                                @AuthenticationPrincipal User user) {

        if (Arrays.stream(Category.values()).noneMatch((t) -> t.name().equals(category)) || weight <= 0 || name.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal arguments");
        }
            if (date.isEmpty()) {
                return itemService.saveOrUpdateCustomItemWithoutDate(new CustomItem(name,weight, Category.valueOf(category),user));
            } else {
               return itemService.saveOrUpdateCustomItemWithDate(new CustomItem(name,weight,Category.valueOf(category),user), LocalDate.parse(date));
            }
    }

    /*
    Get custom items by user
     */
    @GetMapping("/getCustom")
    public ResponseEntity<List<CustomItem>>  getCustomItemsByUser(@AuthenticationPrincipal User user){
        return itemService.getCustomItemByUser(user);
    }
}
