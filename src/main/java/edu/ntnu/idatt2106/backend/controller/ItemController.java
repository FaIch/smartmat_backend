package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.ItemEntity;
import edu.ntnu.idatt2106.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/api/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    //Todo: create upload endpoint(??) when have user and know what parameters

    @GetMapping("/list")
    public List<ItemEntity> list(){
        return itemService.getAllItems();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<ItemEntity> getItemById(@PathVariable Long id){
        ItemEntity item = itemService.getItem(id);
        if(item == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            return ResponseEntity.ok(item);
        }
    }

  /*  @GetMapping("/list/me")
    public List<ItemEntity> getItemsByUser(@AuthenticationPrincipal UserEntity user){
        return itemService.getItemByUser(user);
    }*/

    @GetMapping("/list/category")
    public List<ItemEntity> getItemsByCategory(@RequestParam("category") String category){
        return itemService.getItemByCategory(category);
    }

  /*  @GetMapping("categories")
    public List<Category> getCategories(){
        return Arrays.stream(Category.values()).toList();
    }
*/
}
