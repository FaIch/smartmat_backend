package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.CustomItem;
import edu.ntnu.idatt2106.backend.model.item.Item;
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

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping("/list")
    public List<Item> list(){
        return itemService.getAllItems();
    }

    @GetMapping("/list/category")
    public List<Item> getItemsByCategory(@RequestParam("category") String category){
        return itemService.getItemByCategory(category);
    }

   @GetMapping("categories")
    public List<Category> getCategories(){
        return Arrays.stream(Category.values()).toList();
    }

  @PostMapping("/addCustom")
  public ResponseEntity<?> uploadCustomItem(@RequestParam("name")String name, @RequestParam("weight") double weight,
                                            @RequestParam("date") String date,  @AuthenticationPrincipal User user) {
      itemService.saveOrUpdateCustomItem(new CustomItem(name,weight,LocalDate.parse(date), user));
      return ResponseEntity.ok().build();
  }

  @GetMapping("/getCustom")
  public List<CustomItem>  getCustomItemsByUser(@AuthenticationPrincipal User user){
      return itemService.getCustomItemByUser(user);
  }
}
