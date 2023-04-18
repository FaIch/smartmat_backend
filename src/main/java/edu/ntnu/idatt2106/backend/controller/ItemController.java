package edu.ntnu.idatt2106.backend.controller;

import edu.ntnu.idatt2106.backend.model.item.Category;
import edu.ntnu.idatt2106.backend.model.item.Item;
import edu.ntnu.idatt2106.backend.model.user.User;
import edu.ntnu.idatt2106.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
@RequestMapping(value = "/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("name")String name, @RequestParam("desc") String desc, @RequestParam("category") String category,
                                    @RequestParam("price") double price, @RequestParam("image") MultipartFile image/*, @AuthenticationPrincipal User user*/) throws IOException {
        itemService.saveOrUpdate(new Item(name, desc, Category.valueOf(category),
               price/*,user*/, Base64.getEncoder().encodeToString(image.getBytes())));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public List<Item> list(){
        return itemService.getAllItems();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id){
        Item item = itemService.getItem(id);
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
    public List<Item> getItemsByCategory(@RequestParam("category") String category){
        return itemService.getItemByCategory(category);
    }

  /*  @GetMapping("categories")
    public List<Category> getCategories(){
        return Arrays.stream(Category.values()).toList();
    }
*/
}
