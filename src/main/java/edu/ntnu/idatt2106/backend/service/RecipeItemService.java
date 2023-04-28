package edu.ntnu.idatt2106.backend.service;

import edu.ntnu.idatt2106.backend.repository.RecipeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeItemService {

    @Autowired
    private RecipeItemRepository recipeItemRepository;

    public List<Long> getItemIdsByRecipeId(Long recipeId) {
        return recipeItemRepository.findItemIdsByRecipeId(recipeId);
    }
}