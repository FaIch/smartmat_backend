package edu.ntnu.idatt2106.backend.model.recipe;

public class RecipeWithFridgeCount implements Comparable<RecipeWithFridgeCount> {
    private final Recipe recipe;
    private final int fridgeCount;

    public RecipeWithFridgeCount(Recipe recipe, int fridgeCount) {
        this.recipe = recipe;
        this.fridgeCount = fridgeCount;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public int getFridgeCount() {
        return fridgeCount;
    }

    @Override
    public int compareTo(RecipeWithFridgeCount other) {
        return Integer.compare(other.fridgeCount, this.fridgeCount);
    }
}