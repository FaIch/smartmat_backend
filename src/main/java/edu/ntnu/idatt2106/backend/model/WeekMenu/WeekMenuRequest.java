package edu.ntnu.idatt2106.backend.model.WeekMenu;


import java.util.List;
public class WeekMenuRequest {
    private List<Long> recipeItemsId;

    public WeekMenuRequest(List<Long> recipeItemsId) {
        this.recipeItemsId = recipeItemsId;
    }

    public List<Long> getRecipeItemsId() {
        return recipeItemsId;
    }

    public void setRecipeItemsId(List<Long> recipeItemsId) {
        this.recipeItemsId = recipeItemsId;
    }
}