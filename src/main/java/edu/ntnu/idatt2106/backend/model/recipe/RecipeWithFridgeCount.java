package edu.ntnu.idatt2106.backend.model.recipe;

public class RecipeWithFridgeCount{
    private Recipe recipe;
    private final int amountInFridge;
    private final int amountNearlyExpired;

    public RecipeWithFridgeCount(Recipe recipe, int amountInFridge, int amountNearlyExpired) {
        this.recipe = recipe;
        this.amountInFridge = amountInFridge;
        this.amountNearlyExpired = amountNearlyExpired;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public int getAmountInFridge() {
        return amountInFridge;
    }

    public int getAmountNearlyExpired() {
        return amountNearlyExpired;
    }

}