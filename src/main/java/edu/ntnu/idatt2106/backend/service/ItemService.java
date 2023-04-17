package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.model.ItemEntity;

import java.util.List;

public interface ItemService {


    public List<ItemEntity> getAllItems();

    public ItemEntity getItem(Long id);


   // public List<ItemEntity> getItemByUser(UserEntity user);


    List<ItemEntity> getItemByCategory(String category);

    public void saveOrUpdate(ItemEntity itemEntity);

}
