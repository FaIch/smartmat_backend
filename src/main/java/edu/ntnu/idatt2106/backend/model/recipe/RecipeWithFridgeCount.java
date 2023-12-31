package edu.ntnu.idatt2106.backend.model.recipe;

public class RecipeWithFridgeCount{
    private final long id;
    private Recipe recipe;
    private final int amountInFridge;
    private final int amountNearlyExpired;

    public RecipeWithFridgeCount(long id, Recipe recipe, int amountInFridge, int amountNearlyExpired) {
        this.id = id;
        this.recipe = recipe;
        this.amountInFridge = amountInFridge;
        this.amountNearlyExpired = amountNearlyExpired;
    }

    public long getId() {
        return id;
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